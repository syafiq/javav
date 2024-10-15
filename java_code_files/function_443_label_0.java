    public void testSniHandlerFailsOnTooBigClientHello() throws Exception {
        SniHandler handler = new SniHandler(new Mapping<String, SslContext>() {
            @Override
            public SslContext map(String input) {
                throw new UnsupportedOperationException("Should not be called");
            }
        }, 10, 0);

        final AtomicReference<SniCompletionEvent> completionEventRef =
                new AtomicReference<SniCompletionEvent>();
        final EmbeddedChannel ch = new EmbeddedChannel(handler, new ChannelInboundHandlerAdapter() {
            @Override
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
                if (evt instanceof SniCompletionEvent) {
                    completionEventRef.set((SniCompletionEvent) evt);
                }
            }
        });
        final ByteBuf buffer = ch.alloc().buffer();
        buffer.writeByte(0x16);      // Content Type: Handshake
        buffer.writeShort((short) 0x0303); // TLS 1.2
        buffer.writeShort((short) 0x0006); // Packet length

        // 16_777_215
        buffer.writeByte((byte) 0x01); // Client Hello
        buffer.writeMedium(0xFFFFFF); // Length
        buffer.writeShort((short) 0x0303); // TLS 1.2

        assertThrows(TooLongFrameException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                ch.writeInbound(buffer);
            }
        });
        try {
            while (completionEventRef.get() == null) {
                Thread.sleep(100);
                // We need to run all pending tasks as the handshake timeout is scheduled on the EventLoop.
                ch.runPendingTasks();
            }
            SniCompletionEvent completionEvent = completionEventRef.get();
            assertNotNull(completionEvent);
            assertNotNull(completionEvent.cause());
            assertEquals(TooLongFrameException.class, completionEvent.cause().getClass());
        } finally {
            ch.finishAndReleaseAll();
        }
    }