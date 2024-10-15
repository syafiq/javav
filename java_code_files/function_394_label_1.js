    public void protocolMismatchHeader() throws IOException {
        ByteBuffer[] buffers = new ByteBuffer[] {
            ByteBuffer.wrap(new byte[] { 'A' }),
            ByteBuffer.wrap(new byte[] { 'A', 'M', 'Q' }),
            ByteBuffer.wrap(new byte[] { 'A', 'N', 'Q', 'P' }),
            ByteBuffer.wrap(new byte[] { 'A', 'M', 'Q', 'P' }),
            ByteBuffer.wrap(new byte[] { 'A', 'M', 'Q', 'P', 1, 1, 8 }),
            ByteBuffer.wrap(new byte[] { 'A', 'M', 'Q', 'P', 1, 1, 8, 0 }),
            ByteBuffer.wrap(new byte[] { 'A', 'M', 'Q', 'P', 1, 1, 9, 1 })
        };
        String[] messages = new String[] {
            "Invalid AMQP protocol header from server: read only 1 byte(s) instead of 4",
            "Invalid AMQP protocol header from server: read only 3 byte(s) instead of 4",
            "Invalid AMQP protocol header from server: expected character 77, got 78",
            "Invalid AMQP protocol header from server",
            "Invalid AMQP protocol header from server",
            "AMQP protocol version mismatch; we are version 0-9-1, server is 0-8",
            "AMQP protocol version mismatch; we are version 0-9-1, server sent signature 1,1,9,1"
        };

        for (int i = 0; i < buffers.length; i++) {
            builder = new FrameBuilder(channel, buffers[i]);
            try {
                builder.readFrame();
                fail("protocol header not correct, exception should have been thrown");
            } catch (MalformedFrameException e) {
                assertThat(e.getMessage()).isEqualTo(messages[i]);
            }
        }
    }