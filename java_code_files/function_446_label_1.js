    public void testSslHandlerFiresHandshakeTimeout(SslProvider provider) throws Exception {
        final SslContext context = makeSslContext(provider, false);
        SniHandler handler = new SniHandler(new Mapping<String, SslContext>() {
            @Override
            public SslContext map(String input) {
                return context;
            }
        }, 100);

        final AtomicReference<SniCompletionEvent> sniCompletionEventRef =
            new AtomicReference<SniCompletionEvent>();
        final AtomicReference<SslHandshakeCompletionEvent> handshakeCompletionEventRef =
            new AtomicReference<SslHandshakeCompletionEvent>();
        EmbeddedChannel ch = new EmbeddedChannel(handler, new ChannelInboundHandlerAdapter() {
            @Override
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
                if (evt instanceof SniCompletionEvent) {
                    sniCompletionEventRef.set((SniCompletionEvent) evt);
                } else if (evt instanceof SslHandshakeCompletionEvent) {
                    handshakeCompletionEventRef.set((SslHandshakeCompletionEvent) evt);
                }
            }
        });
        try {
            // Send enough data to add the SslHandler and let the handshake incomplete
            // Client Hello with "host1" server name
            ch.writeInbound(Unpooled.wrappedBuffer(StringUtil.decodeHexDump(
                "16030301800100017c0303478ae7e536aa7a9debad1f873121862d2d3d3173e0ef42975c31007faeb2" +
                "52522047f55f81fc84fe58951e2af14026147d6178498fde551fcbafc636462c016ec9005a13011302" +
                "c02cc02bc030009dc02ec032009f00a3c02f009cc02dc031009e00a2c024c028003dc026c02a006b00" +
                "6ac00ac0140035c005c00f00390038c023c027003cc025c02900670040c009c013002fc004c00e0033" +
                "003200ff010000d90000000a0008000005686f737431000500050100000000000a00160014001d0017" +
                "00180019001e01000101010201030104000b00020100000d0028002604030503060308040805080608" +
                "09080a080b040105010601040203030301030202030201020200320028002604030503060308040805" +
                "08060809080a080b040105010601040203030301030202030201020200110009000702000400000000" +
                "00170000002b00050403040303002d00020101003300260024001d00200bbc37375e214c1e4e7cb90f" +
                "869e131dc983a21f8205ba24456177f340904935")));

            while (handshakeCompletionEventRef.get() == null) {
                Thread.sleep(10);
                // We need to run all pending tasks as the handshake timeout is scheduled on the EventLoop.
                ch.runPendingTasks();
            }
            SniCompletionEvent sniCompletionEvent = sniCompletionEventRef.get();
            assertNotNull(sniCompletionEvent);
            assertEquals("host1", sniCompletionEvent.hostname());
            SslCompletionEvent handshakeCompletionEvent = handshakeCompletionEventRef.get();
            assertNotNull(handshakeCompletionEvent);
            assertNotNull(handshakeCompletionEvent.cause());
            assertEquals(SslHandshakeTimeoutException.class, handshakeCompletionEvent.cause().getClass());
        } finally {
            ch.finishAndReleaseAll();
            releaseAll(context);
        }
    }