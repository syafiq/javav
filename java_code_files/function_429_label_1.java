    protected AbstractFrameHandlerFactory(int connectionTimeout, SocketConfigurator configurator, boolean ssl) {
        this.connectionTimeout = connectionTimeout;
        this.configurator = configurator;
        this.ssl = ssl;
    }