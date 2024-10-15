    public HttpPostStandardRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset,
                                          int maxFields, int maxBufferedBytes) {
        this.request = checkNotNull(request, "request");
        this.charset = checkNotNull(charset, "charset");
        this.factory = checkNotNull(factory, "factory");
        this.maxFields = maxFields;
        this.maxBufferedBytes = maxBufferedBytes;
        try {
            if (request instanceof HttpContent) {
                // Offer automatically if the given request is as type of HttpContent
                // See #1089
                offer((HttpContent) request);
            } else {
                parseBody();
            }
        } catch (Throwable e) {
            destroy();
            PlatformDependent.throwException(e);
        }
    }