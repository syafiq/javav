    public HttpClientBuilder create()
    {
        HttpClientBuilder result = HttpClientBuilder.create();
        result.useSystemProperties();
        result.setUserAgent("XWikiHTMLDiff");

        // Set the connection timeout.
        Timeout timeout = Timeout.ofSeconds(this.configuration.getHTTPTimeout());
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setConnectTimeout(timeout)
            .setSocketTimeout(timeout)
            .build();

        BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
        cm.setConnectionConfig(connectionConfig);
        result.setConnectionManager(cm);

        // Set the response timeout.
        result.setDefaultRequestConfig(RequestConfig.custom().setResponseTimeout(timeout).build());

        return result;
    }