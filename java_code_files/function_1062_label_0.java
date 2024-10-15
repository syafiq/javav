    public void testFieldGreaterThanMaxBufferedBytesStandardDecoder() {
        HttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/");

        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req, -1, 6);

        decoder.offer(new DefaultHttpContent(Unpooled.wrappedBuffer("foo=bar".getBytes())));
    }