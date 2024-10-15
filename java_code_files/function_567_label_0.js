    void throwsExceptionWhenImageDownloaderFails() throws URISyntaxException, IOException
    {
        String url = "/image.png";
        URI uri = new URI(URL_PREFIX + url);
        when(this.urlSecurityManager.isDomainTrusted(uri.toURL())).thenReturn(true);
        IOException exception = new IOException("Failed to download image.");
        when(this.imageDownloader.download(uri)).thenThrow(exception);

        DiffException thrown = assertThrows(DiffException.class, () -> this.converter.convert(url));
        assertEquals(getFailureMessage(url), thrown.getMessage());
        assertEquals(exception, thrown.getCause());

        // Make sure the failure is cached.
        verify(this.failureCache).set(CACHE_PREFIX + URL_PREFIX + url, thrown);
        // Make sure nothing is stored in the data cache.
        verify(this.cache, never()).set(any(), any());
    }