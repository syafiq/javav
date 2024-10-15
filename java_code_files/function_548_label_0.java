    void throwsExceptionWhenURLIsMalFormed() throws IOException
    {
        String url = "http://w w w.example.com";
        when(this.urlSecurityManager.isDomainTrusted(new URL(url))).thenReturn(true);
        DiffException exception = assertThrows(DiffException.class, () -> this.converter.convert(url));
        assertEquals(getFailureMessage(url), exception.getMessage());
        assertEquals("Illegal character in authority at index 7: http://w w w.example.com",
            exception.getCause().getMessage());

        verify(this.imageDownloader, never()).download(any());
        verify(this.cache, never()).set(any(), any());
        verify(this.failureCache).set(CACHE_PREFIX + url,
            exception);
    }