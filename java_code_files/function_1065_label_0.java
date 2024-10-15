    public HttpPostRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset,
                                  int maxFields, int maxBufferedBytes) {
        ObjectUtil.checkNotNull(factory, "factory");
        ObjectUtil.checkNotNull(request, "request");
        ObjectUtil.checkNotNull(charset, "charset");

        // Fill default values
        if (isMultipart(request)) {
            decoder = new HttpPostMultipartRequestDecoder(factory, request, charset, maxFields, maxBufferedBytes);
        } else {
            decoder = new HttpPostStandardRequestDecoder(factory, request, charset, maxFields, maxBufferedBytes);
        }
    }