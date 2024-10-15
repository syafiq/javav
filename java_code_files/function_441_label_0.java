    public SniHandler(Mapping<? super String, ? extends SslContext> mapping,
                      int maxClientHelloLength, long handshakeTimeoutMillis) {
        this(new AsyncMappingAdapter(mapping), maxClientHelloLength, handshakeTimeoutMillis);
    }