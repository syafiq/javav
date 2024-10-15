    public void testSniHandlerFiresHandshakeTimeout() throws Exception {
        SniHandler handler = new SniHandler(new Mapping<String, SslContext>() {
            @Override
            public SslContext map(String input) {
                throw new UnsupportedOperationException("Should not be called");
            }
        }, 0, 10);

        final AtomicReference<SniCompletionEvent> completionEventRef =
            new AtomicReference<SniCompletionEvent>();
        EmbeddedChannel ch = new EmbeddedChannel(handler, new ChannelInboundHandlerAdapter() {
            @Override
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
                if (evt instanceof SniCompletionEvent) {
                    completionEventRef.set((SniCompletionEvent) evt);
                }
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
            assertEquals(SslHandshakeTimeoutException.class, completionEvent.cause().getClass());
        } finally {
            ch.finishAndReleaseAll();
        }
    }