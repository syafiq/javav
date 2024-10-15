    public void testTooLongFormFieldMultipartDecoder() {
        HttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/");
        req.headers().add("Content-Type", "multipart/form-data;boundary=be38b42a9ad2713f");

        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req, -1, 16 * 1024);

        try {
            decoder.offer(new DefaultHttpContent(Unpooled.wrappedBuffer(new byte[16 * 1024 + 1])));
            fail();
        } catch (DecoderException e) {
            assertEquals(HttpPostRequestDecoder.TooLongFormFieldException.class, e.getClass());
        }
    }