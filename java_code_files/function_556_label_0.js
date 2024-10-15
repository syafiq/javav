    public int getHTTPTimeout()
    {
        return this.configurationSource.getProperty(getFullKeyName("httpTimeout"), Integer.class, 10);
    }