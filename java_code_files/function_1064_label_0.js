    public HttpPostMultipartRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset,
                                           int maxFields, int maxBufferedBytes) {
        this.request = checkNotNull(request, "request");
        this.charset = checkNotNull(charset, "charset");
        this.factory = checkNotNull(factory, "factory");
        this.maxFields = maxFields;
        this.maxBufferedBytes = maxBufferedBytes;
        // Fill default values

        String contentTypeValue = this.request.headers().get(HttpHeaderNames.CONTENT_TYPE);
        if (contentTypeValue == null) {
            throw new ErrorDataDecoderException("No '" + HttpHeaderNames.CONTENT_TYPE + "' header present.");
        }

        String[] dataBoundary = HttpPostRequestDecoder.getMultipartDataBoundary(contentTypeValue);
        if (dataBoundary != null) {
            multipartDataBoundary = dataBoundary[0];
            if (dataBoundary.length > 1 && dataBoundary[1] != null) {
                try {
                    this.charset = Charset.forName(dataBoundary[1]);
                } catch (IllegalCharsetNameException e) {
                    throw new ErrorDataDecoderException(e);
                }
            }
        } else {
            multipartDataBoundary = null;
        }
        currentStatus = MultiPartStatus.HEADERDELIMITER;

        try {
            if (request instanceof HttpContent) {
                // Offer automatically if the given request is als type of HttpContent
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