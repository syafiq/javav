    public SslEngineFrameBuilder(SSLEngine sslEngine, ByteBuffer plainIn, ByteBuffer cipherIn, ReadableByteChannel channel) {
        super(channel, plainIn);
        this.sslEngine = sslEngine;
        this.cipherBuffer = cipherIn;
    }