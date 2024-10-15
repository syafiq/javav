    public void dispose()
    {
        if (this.cache != null) {
            this.cache.dispose();
        }
        if (this.failureCache != null) {
            this.failureCache.dispose();
        }
    }