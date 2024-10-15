    public AMQConnection(ConnectionParams params, FrameHandler frameHandler, MetricsCollector metricsCollector)
    {
        checkPreconditions();
        this.credentialsProvider = params.getCredentialsProvider();
        this._frameHandler = frameHandler;
        this._virtualHost = params.getVirtualHost();
        this._exceptionHandler = params.getExceptionHandler();

        this._clientProperties = new HashMap<>(params.getClientProperties());
        this.requestedFrameMax = params.getRequestedFrameMax();
        this.requestedChannelMax = params.getRequestedChannelMax();
        this.requestedHeartbeat = params.getRequestedHeartbeat();
        this.handshakeTimeout = params.getHandshakeTimeout();
        this.shutdownTimeout = params.getShutdownTimeout();
        this.saslConfig = params.getSaslConfig();
        this.consumerWorkServiceExecutor = params.getConsumerWorkServiceExecutor();
        this.heartbeatExecutor = params.getHeartbeatExecutor();
        this.shutdownExecutor = params.getShutdownExecutor();
        this.threadFactory = params.getThreadFactory();
        if(params.getChannelRpcTimeout() < 0) {
            throw new IllegalArgumentException("Continuation timeout on RPC calls cannot be less than 0");
        }
        this.channelRpcTimeout = params.getChannelRpcTimeout();
        this.channelShouldCheckRpcResponseType = params.channelShouldCheckRpcResponseType();

        this.trafficListener = params.getTrafficListener() == null ? TrafficListener.NO_OP : params.getTrafficListener();

        this.credentialsRefreshService = params.getCredentialsRefreshService();


        this._channel0 = createChannel0();

        this._channelManager = null;

        this._brokerInitiatedShutdown = false;

        this._inConnectionNegotiation = true; // we start out waiting for the first protocol response

        this.metricsCollector = metricsCollector;

        this.errorOnWriteListener = params.getErrorOnWriteListener() != null ? params.getErrorOnWriteListener() :
            (connection, exception) -> { throw exception; }; // we just propagate the exception for non-recoverable connections
        this.workPoolTimeout = params.getWorkPoolTimeout();
        this.maxInboundMessageBodySize = params.getMaxInboundMessageBodySize();
    }