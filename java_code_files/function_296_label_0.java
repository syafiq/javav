    public CustomObjectInputStream(ClassLoader classLoader, InputStream in,Set<String> allowedClasses) throws IOException {
        super(in);
        this.classLoader = classLoader;
        this.allowedClasses = allowedClasses;
    }