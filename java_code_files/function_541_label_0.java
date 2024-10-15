    void passesCookiesFromRequest(String requestDomain, boolean shouldSendCookie, String cookieDomain)
        throws IOException
    {
        // Set a mock request in the context.
        XWikiRequest request = mock();
        when(request.getServerName()).thenReturn(requestDomain);
        String cookieHeader = "cookie1=value1; cookie2=value2";
        when(request.getHeader("Cookie")).thenReturn(cookieHeader);
        when(this.xwikiContext.getRequest()).thenReturn(request);

        if (StringUtils.isNotBlank(cookieDomain)) {
            when(this.authenticationConfiguration.getCookieDomains()).thenReturn(List.of(cookieDomain));
        }

        // Trigger the download.
        byte[] content = new byte[] { 1, 2, 3 };
        when(this.httpEntity.getContent()).thenReturn(new ByteArrayInputStream(content));
        this.imageDownloader.download(IMAGE_URI);

        ArgumentCaptor<ClassicHttpRequest> requestCaptor = ArgumentCaptor.forClass(ClassicHttpRequest.class);
        verify(this.httpClient).execute(requestCaptor.capture(), any(HttpClientResponseHandler.class));

        // Verify that the cookies are passed to the HTTP client if it should do so.
        ClassicHttpRequest httpRequest = requestCaptor.getValue();
        Header[] headers = httpRequest.getHeaders("Cookie");
        if (shouldSendCookie) {
            assertEquals(1, headers.length);
            assertEquals(cookieHeader, headers[0].getValue());
        } else {
            assertEquals(0, headers.length);
        }
    }