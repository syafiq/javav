    public URI toURI() {
        try {
            return new URI(
                    "jdbc:" + databaseType.toLowerCase(),
                    databaseHost + ((databasePort == 0) ? "" : (":" + databasePort)),
                    "/" + databaseName,
                    useSSL ? "useSSL=true" : null,
                    null
            );
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }