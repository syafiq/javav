    public SniHandler(AsyncMapping<? super String, ? extends SslContext> mapping, long handshakeTimeoutMillis) {
        this(mapping, 0, handshakeTimeoutMillis);
    }