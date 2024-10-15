    public SniHandler(Mapping<? super String, ? extends SslContext> mapping, long handshakeTimeoutMillis) {
        this(new AsyncMappingAdapter(mapping), handshakeTimeoutMillis);
    }