    private HttpEntity fetch(URI uri) throws IOException
    {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.useSystemProperties();
        httpClientBuilder.setUserAgent("XWikiHTMLDiff");

        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpGet getMethod = new HttpGet(uri);

        XWikiRequest request = this.xcontextProvider.get().getRequest();
        if (request != null) {
            // Copy the cookies from the current request.
            getMethod.setHeader(HEADER_COOKIE, request.getHeader(HEADER_COOKIE));
        }

        CloseableHttpResponse response = httpClient.execute(getMethod);
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
            return response.getEntity();
        } else {
            throw new IOException(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
        }
    }