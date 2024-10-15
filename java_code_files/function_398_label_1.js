    public void buildFramesInOneGo() throws IOException {
        byte[] frameContent = new byte[] { 1, 0, 0, 0, 0, 0, 3, 1, 2, 3, end() };
        int nbFrames = 13;
        byte[] frames = new byte[frameContent.length * nbFrames];
        for (int i = 0; i < nbFrames; i++) {
            for (int j = 0; j < frameContent.length; j++) {
                frames[i * frameContent.length + j] = frameContent[j];
            }
        }
        buffer = ByteBuffer.wrap(frames);
        builder = new FrameBuilder(channel, buffer);
        int frameCount = 0;
        Frame frame;
        while ((frame = builder.readFrame()) != null) {
            assertThat(frame).isNotNull();
            assertThat(frame.type).isEqualTo(1);
            assertThat(frame.channel).isEqualTo(0);
            assertThat(frame.getPayload()).hasSize(3);
            frameCount++;
        }
        assertThat(frameCount).isEqualTo(nbFrames);
    }