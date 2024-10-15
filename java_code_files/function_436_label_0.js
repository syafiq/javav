    protected AbstractSniHandler(int maxClientHelloLength, long handshakeTimeoutMillis) {
        super(maxClientHelloLength);
        this.handshakeTimeoutMillis = checkPositiveOrZero(handshakeTimeoutMillis, "handshakeTimeoutMillis");
    }