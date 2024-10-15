    public void testDecorationWithNull() {
        Http2ConnectionDecoder delegate = mock(Http2ConnectionDecoder.class);

        Http2EmptyDataFrameConnectionDecoder decoder = new Http2EmptyDataFrameConnectionDecoder(delegate, 2);
        decoder.frameListener(null);
        assertNull(decoder.frameListener());
    }