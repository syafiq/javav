    public void buildFrameInOneGo() throws IOException {
        buffer = ByteBuffer.wrap(new byte[] { 1, 0, 0, 0, 0, 0, 3, 1, 2, 3, end() });
        builder = new FrameBuilder(channel, buffer);
        Frame frame = builder.readFrame();
        assertThat(frame).isNotNull();
        assertThat(frame.type).isEqualTo(1);
        assertThat(frame.channel).isEqualTo(0);
        assertThat(frame.getPayload()).hasSize(3);
    }