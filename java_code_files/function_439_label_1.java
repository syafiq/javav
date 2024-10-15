    public SniHandler(AsyncMapping<? super String, ? extends SslContext> mapping, long handshakeTimeoutMillis) {
        super(handshakeTimeoutMillis);
        this.mapping = (AsyncMapping<String, SslContext>) ObjectUtil.checkNotNull(mapping, "mapping");
    }