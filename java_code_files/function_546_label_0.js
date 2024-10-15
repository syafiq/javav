    void usesCacheWhenAvailable() throws DiffException
    {
        String dataURI = "data:image/png;base64,def";
        String url = "/image.png";
        when(this.cache.get(CACHE_PREFIX + URL_PREFIX + url)).thenReturn(dataURI);

        assertEquals(dataURI, this.converter.convert(url));
    }