    public void testDecoration() {
        Http2ConnectionDecoder delegate = mock(Http2ConnectionDecoder.class);
        final ArgumentCaptor<Http2FrameListener> listenerArgumentCaptor =
                ArgumentCaptor.forClass(Http2FrameListener.class);
        when(delegate.frameListener()).then(new Answer<Http2FrameListener>() {
            @Override
            public Http2FrameListener answer(InvocationOnMock invocationOnMock) {
                return listenerArgumentCaptor.getValue();
            }
        });
        Http2FrameListener listener = mock(Http2FrameListener.class);
        Http2EmptyDataFrameConnectionDecoder decoder = new Http2EmptyDataFrameConnectionDecoder(delegate, 2);
        decoder.frameListener(listener);
        verify(delegate).frameListener(listenerArgumentCaptor.capture());

        assertThat(decoder.frameListener(),
                CoreMatchers.not(CoreMatchers.instanceOf(Http2EmptyDataFrameListener.class)));
        assertThat(decoder.frameListener0(), CoreMatchers.instanceOf(Http2EmptyDataFrameListener.class));
    }