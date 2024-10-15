    void returnsContent(long contentLength) throws IOException
    {
        // Set different content lengths to test the different code paths.
        when(this.httpEntity.getContentLength()).thenReturn(contentLength);
        if (contentLength < 200) {
            when(this.configuration.getMaximumContentSize()).thenReturn(200L);
        } else {
            // Test unlimited size.
            when(this.configuration.getMaximumContentSize()).thenReturn(0L);
        }
        byte[] content = new byte[] { 1, 2, 3 };
        when(this.httpEntity.getContent()).thenReturn(new ByteArrayInputStream(content));
        ImageDownloader.DownloadResult result = this.imageDownloader.download(IMAGE_URI);
        assertArrayEquals(content, result.getData());
        assertEquals(IMAGE_CONTENT_TYPE, result.getContentType());
    }