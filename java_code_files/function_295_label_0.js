    public SerializationCodec(ClassLoader classLoader, SerializationCodec codec) {
        this.classLoader = classLoader;
        this.allowedClasses = codec.allowedClasses;
    }