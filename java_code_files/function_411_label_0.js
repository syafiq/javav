    public FrameBuilder(ReadableByteChannel channel, ByteBuffer buffer, int maxPayloadSize) {
        this.channel = channel;
        this.applicationBuffer = buffer;
        this.maxPayloadSize = maxPayloadSize;
    }