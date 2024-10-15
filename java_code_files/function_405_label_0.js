    public SocketFrameHandler(Socket socket, ExecutorService shutdownExecutor,
                              int maxInboundMessageBodySize) throws IOException {
        _socket = socket;
        _shutdownExecutor = shutdownExecutor;
        this.maxInboundMessageBodySize = maxInboundMessageBodySize;

        _inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        _outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }