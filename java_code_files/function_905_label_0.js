    void before() throws Exception
    {
        Cache<Object> cache = mock(Cache.class);
        when(this.cacheManager.createNewCache(any(CacheConfiguration.class))).thenReturn(cache);
    }