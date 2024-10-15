    void returnsDataURIForReturnedDataAndMimeType() throws IOException, DiffException
    {
        String url = "/image.png";
        URI uri = URI.create(URL_PREFIX + url);
        when(this.urlSecurityManager.isDomainTrusted(uri.toURL())).thenReturn(true);
        String dataURI = "data:image/jpeg;base64,ZGVm";
        when(this.imageDownloader.download(uri))
            .thenReturn(new ImageDownloader.DownloadResult(new byte[] { 'd', 'e', 'f' }, "image/jpeg"));

        assertEquals(dataURI, this.converter.convert(url));

        // Make sure the data is cached.
        verify(this.cache).set(CACHE_PREFIX + URL_PREFIX + url, dataURI);
        // Make sure nothing is stored in the failure cache.
        verify(this.failureCache, never()).set(any(), any());
    }