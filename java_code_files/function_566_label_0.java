    void throwsCachedFailure()
    {
        String url = "/image.png";
        DiffException exception = new DiffException("Failed to convert url to absolute URL.");
        when(this.failureCache.get(CACHE_PREFIX + URL_PREFIX + url)).thenReturn(exception);

        DiffException thrown = assertThrows(DiffException.class, () -> this.converter.convert(url));
        assertEquals(exception, thrown);
    }