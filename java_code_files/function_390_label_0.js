    public SslEngineFrameBuilder(SSLEngine sslEngine, ByteBuffer plainIn,
                                 ByteBuffer cipherIn, ReadableByteChannel channel,
                                 int maxPayloadSize) {
        super(channel, plainIn, maxPayloadSize);
        this.sslEngine = sslEngine;
        this.cipherBuffer = cipherIn;
    }