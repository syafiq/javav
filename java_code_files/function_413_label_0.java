    public SocketFrameHandlerFactory(int connectionTimeout, SocketFactory socketFactory, SocketConfigurator configurator,
                                     boolean ssl, ExecutorService shutdownExecutor, SslContextFactory sslContextFactory,
                                     int maxInboundMessageBodySize) {
        super(connectionTimeout, configurator, ssl, maxInboundMessageBodySize);
        this.socketFactory = socketFactory;
        this.shutdownExecutor = shutdownExecutor;
        this.sslContextFactory = sslContextFactory;
    }