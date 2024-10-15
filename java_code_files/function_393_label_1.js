    public SocketChannelFrameHandlerFactory(int connectionTimeout, NioParams nioParams, boolean ssl, SslContextFactory sslContextFactory) {
        super(connectionTimeout, null, ssl);
        this.nioParams = new NioParams(nioParams);
        this.sslContextFactory = sslContextFactory;
        this.nioLoopContexts = new ArrayList<>(this.nioParams.getNbIoThreads());
        for (int i = 0; i < this.nioParams.getNbIoThreads(); i++) {
            this.nioLoopContexts.add(new NioLoopContext(this, this.nioParams));
        }
    }