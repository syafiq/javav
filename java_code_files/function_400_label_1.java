    public SocketFrameHandlerFactory(int connectionTimeout, SocketFactory socketFactory, SocketConfigurator configurator,
                                     boolean ssl, ExecutorService shutdownExecutor, SslContextFactory sslContextFactory) {
        super(connectionTimeout, configurator, ssl);
        this.socketFactory = socketFactory;
        this.shutdownExecutor = shutdownExecutor;
        this.sslContextFactory = sslContextFactory;
    }