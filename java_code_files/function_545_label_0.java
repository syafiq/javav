    void throwsWhenNonImageContentType()
    {
        when(this.httpEntity.getContentType()).thenReturn("text/html");
        IOException ioException = assertThrows(IOException.class, () -> this.imageDownloader.download(IMAGE_URI));
        assertEquals(String.format("The content of [%s] is not an image.", IMAGE_URI), ioException.getMessage());
    }