    public AMQCommand(com.rabbitmq.client.Method method, AMQContentHeader contentHeader, byte[] body) {
        this.assembler = new CommandAssembler((Method) method, contentHeader, body, Integer.MAX_VALUE);
    }