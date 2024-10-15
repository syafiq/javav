    public SocketFrameHandler(Socket socket, ExecutorService shutdownExecutor) throws IOException {
        _socket = socket;
        _shutdownExecutor = shutdownExecutor;

        _inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        _outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }