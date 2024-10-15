    public CommandAssembler(Method method, AMQContentHeader contentHeader, byte[] body,
                            int maxBodyLength) {
        this.method = method;
        this.contentHeader = contentHeader;
        this.bodyN = new ArrayList<>(2);
        this.bodyLength = 0;
        this.remainingBodyBytes = 0;
        this.maxBodyLength = maxBodyLength;
        appendBodyFragment(body);
        if (method == null) {
            this.state = CAState.EXPECTING_METHOD;
        } else if (contentHeader == null) {
            this.state = method.hasContent() ? CAState.EXPECTING_CONTENT_HEADER : CAState.COMPLETE;
        } else {
            this.remainingBodyBytes = contentHeader.getBodySize() - this.bodyLength;
            updateContentBodyState();
        }
    }