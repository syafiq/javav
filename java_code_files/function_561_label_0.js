    public DownloadResult download(URI uri) throws IOException
    {
        HttpClientBuilder httpClientBuilder = this.httpClientBuilderFactory.create();

        HttpGet getMethod = initializeGetMethod(uri);

        try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
            return httpClient.execute(getMethod, response -> handleResponse(uri, response));
        }
    }