    public void testTooManyFormFieldsPostMultipartDecoder() {
        HttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/");
        req.headers().add("Content-Type", "multipart/form-data;boundary=be38b42a9ad2713f");

        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req, 1024, -1);
        decoder.offer(new DefaultHttpContent(Unpooled.wrappedBuffer("--be38b42a9ad2713f\n".getBytes())));

        int num = 0;
        while (true) {
            try {
                byte[] bodyBytes = ("content-disposition: form-data; name=\"title\"\n" +
                        "content-length: 10\n" +
                        "content-type: text/plain; charset=UTF-8\n" +
                        "\n" +
                        "bar-stream\n" +
                        "--be38b42a9ad2713f\n").getBytes();
                ByteBuf content = Unpooled.wrappedBuffer(bodyBytes);
                decoder.offer(new DefaultHttpContent(content));
            } catch (DecoderException e) {
                assertEquals(HttpPostRequestDecoder.TooManyFormFieldsException.class, e.getClass());
                break;
            }
            assertTrue(num++ < 1024);
        }
        assertEquals(1024, num);
    }