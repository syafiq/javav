    void throwsWhenURLIsNotTrusted() throws MalformedURLException
    {
        String url = "http://example.com/image.png";
        when(this.urlSecurityManager.isDomainTrusted(new URL(url))).thenReturn(false);

        DiffException thrown = assertThrows(DiffException.class, () -> this.converter.convert(url));
        assertEquals(getFailureMessage(url), thrown.getMessage());
        assertEquals(String.format("The URL [%s] is not trusted.", url), thrown.getCause().getMessage());

        // Make sure that the failure is cached.
        verify(this.failureCache).set(CACHE_PREFIX + url, thrown);
    }