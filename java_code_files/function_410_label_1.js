    public FrameHandler create(Address addr, String connectionName) throws IOException {
        int portNumber = ConnectionFactory.portOrDefault(addr.getPort(), ssl);

        SSLEngine sslEngine = null;
        SocketChannel channel = null;

        try {
            if (ssl) {
                SSLContext sslContext = sslContextFactory.create(connectionName);
                sslEngine = sslContext.createSSLEngine(addr.getHost(), portNumber);
                sslEngine.setUseClientMode(true);
                if (nioParams.getSslEngineConfigurator() != null) {
                    nioParams.getSslEngineConfigurator().configure(sslEngine);
                }
            }

            SocketAddress address = addr.toInetSocketAddress(portNumber);
            // No Sonar: the channel is closed in case of error and it cannot
            // be closed here because it's part of the state of the connection
            // to be returned.
            channel = SocketChannel.open(); //NOSONAR
            channel.configureBlocking(true);
            if(nioParams.getSocketChannelConfigurator() != null) {
                nioParams.getSocketChannelConfigurator().configure(channel);
            }

            channel.socket().connect(address, this.connectionTimeout);


            if (ssl) {
                int initialSoTimeout = channel.socket().getSoTimeout();
                channel.socket().setSoTimeout(this.connectionTimeout);
                sslEngine.beginHandshake();
                try {
                    ReadableByteChannel wrappedReadChannel = Channels.newChannel(
                        channel.socket().getInputStream());
                    WritableByteChannel wrappedWriteChannel = Channels.newChannel(
                        channel.socket().getOutputStream());
                    boolean handshake = SslEngineHelper.doHandshake(
                        wrappedWriteChannel, wrappedReadChannel, sslEngine);
                    if (!handshake) {
                        LOGGER.error("TLS connection failed");
                        throw new SSLException("TLS handshake failed");
                    }
                    channel.socket().setSoTimeout(initialSoTimeout);
                } catch (SSLHandshakeException e) {
                    LOGGER.error("TLS connection failed: {}", e.getMessage());
                    throw e;
                }
                TlsUtils.logPeerCertificateInfo(sslEngine.getSession());
            }

            channel.configureBlocking(false);

            // lock
            stateLock.lock();
            NioLoopContext nioLoopContext = null;
            try {
                long modulo = globalConnectionCount.getAndIncrement() % nioParams.getNbIoThreads();
                nioLoopContext = nioLoopContexts.get((int) modulo);
                nioLoopContext.initStateIfNecessary();
                SocketChannelFrameHandlerState state = new SocketChannelFrameHandlerState(
                    channel,
                    nioLoopContext,
                    nioParams,
                    sslEngine
                );
                state.startReading();
                SocketChannelFrameHandler frameHandler = new SocketChannelFrameHandler(state);
                return frameHandler;
            } finally {
                stateLock.unlock();
            }


        } catch(IOException e) {
            try {
                if(sslEngine != null && channel != null) {
                    SslEngineHelper.close(channel, sslEngine);
                }
                if (channel != null) {
                    channel.close();
                }
            } catch(IOException closingException) {
                // ignore
            }
            throw e;
        }

    }