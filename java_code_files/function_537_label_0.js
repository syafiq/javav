    void throwsOnNon200Status()
    {
        when(this.httpResponse.getCode()).thenReturn(HttpStatus.SC_NOT_FOUND);
        when(this.httpResponse.getReasonPhrase()).thenReturn("Not Found");
        IOException ioException = assertThrows(IOException.class, () -> this.imageDownloader.download(IMAGE_URI));
        assertEquals("404 Not Found", ioException.getMessage());
    }