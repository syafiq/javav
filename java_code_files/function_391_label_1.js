    public SocketChannelFrameHandlerState(SocketChannel channel, NioLoopContext nioLoopsState, NioParams nioParams, SSLEngine sslEngine) {
        this.channel = channel;
        this.readSelectorState = nioLoopsState.readSelectorState;
        this.writeSelectorState = nioLoopsState.writeSelectorState;

        NioContext nioContext = new NioContext(nioParams, sslEngine);

        this.writeQueue = nioParams.getWriteQueueFactory() == null ?
            NioParams.DEFAULT_WRITE_QUEUE_FACTORY.apply(nioContext) :
            nioParams.getWriteQueueFactory().apply(nioContext);

        this.sslEngine = sslEngine;
        if(this.sslEngine == null) {
            this.ssl = false;
            this.plainOut = nioLoopsState.writeBuffer;
            this.cipherOut = null;
            this.plainIn = nioLoopsState.readBuffer;
            this.cipherIn = null;

            this.outputStream = new DataOutputStream(
                new ByteBufferOutputStream(channel, plainOut)
            );

            this.frameBuilder = new FrameBuilder(channel, plainIn);

        } else {
            this.ssl = true;
            this.plainOut = nioParams.getByteBufferFactory().createWriteBuffer(nioContext);
            this.cipherOut = nioParams.getByteBufferFactory().createEncryptedWriteBuffer(nioContext);
            this.plainIn = nioParams.getByteBufferFactory().createReadBuffer(nioContext);
            this.cipherIn = nioParams.getByteBufferFactory().createEncryptedReadBuffer(nioContext);

            this.outputStream = new DataOutputStream(
                new SslEngineByteBufferOutputStream(sslEngine, plainOut, cipherOut, channel)
            );
            this.frameBuilder = new SslEngineFrameBuilder(sslEngine, plainIn, cipherIn, channel);
        }

    }