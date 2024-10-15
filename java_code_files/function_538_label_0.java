    void throwsWhenContentLengthUnknownAndTooBig() throws IOException
    {
        when(this.httpEntity.getContentLength()).thenReturn(-1L);
        InputStream inputStream = new InputStream()
        {
            @Override
            public int read()
            {
                return 1;
            }
        };
        when(this.configuration.getMaximumContentSize()).thenReturn(100L);

        when(this.httpEntity.getContent()).thenReturn(inputStream);
        IOException ioException = assertThrows(IOException.class, () -> this.imageDownloader.download(IMAGE_URI));
        assertEquals(String.format("The content of [%s] is too big.", IMAGE_URI), ioException.getMessage());
    }