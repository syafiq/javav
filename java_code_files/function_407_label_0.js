    public SocketFrameHandlerFactory(int connectionTimeout, SocketFactory socketFactory, SocketConfigurator configurator,
                                     boolean ssl, ExecutorService shutdownExecutor) {
        this(connectionTimeout, socketFactory, configurator, ssl, shutdownExecutor, null,
             Integer.MAX_VALUE);
    }