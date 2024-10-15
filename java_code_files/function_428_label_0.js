    protected AbstractFrameHandlerFactory(int connectionTimeout, SocketConfigurator configurator,
                                          boolean ssl, int maxInboundMessageBodySize) {
        this.connectionTimeout = connectionTimeout;
        this.configurator = configurator;
        this.ssl = ssl;
        this.maxInboundMessageBodySize = maxInboundMessageBodySize;
    }