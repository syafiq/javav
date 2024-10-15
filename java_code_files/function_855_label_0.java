    public void testMaxRstFramesReached() throws Http2Exception {
        listener = new Http2MaxRstFrameListener(frameListener, 1, 10);
        listener.onRstStreamRead(ctx, 1, Http2Error.STREAM_CLOSED.code());

        Http2Exception ex = assertThrows(Http2Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                listener.onRstStreamRead(ctx, 2, Http2Error.STREAM_CLOSED.code());
            }
        });
        assertEquals(Http2Error.ENHANCE_YOUR_CALM, ex.error());
        verify(frameListener, times(1)).onRstStreamRead(eq(ctx), anyInt(), eq(Http2Error.STREAM_CLOSED.code()));
    }