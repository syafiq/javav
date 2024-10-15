    public void buildFrameInSeveralCalls() throws IOException {
        buffer = ByteBuffer.wrap(new byte[] { 1, 0, 0, 0, 0, 0, 3, 1, 2 });
        builder = new FrameBuilder(channel, buffer);
        Frame frame = builder.readFrame();
        assertThat(frame).isNull();

        buffer.clear();
        buffer.put(b(3)).put(end());
        buffer.flip();

        frame = builder.readFrame();
        assertThat(frame).isNotNull();
        assertThat(frame.type).isEqualTo(1);
        assertThat(frame.channel).isEqualTo(0);
        assertThat(frame.getPayload()).hasSize(3);
    }