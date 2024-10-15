    public CommandAssembler(Method method, AMQContentHeader contentHeader, byte[] body) {
        this.method = method;
        this.contentHeader = contentHeader;
        this.bodyN = new ArrayList<byte[]>(2);
        this.bodyLength = 0;
        this.remainingBodyBytes = 0;
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