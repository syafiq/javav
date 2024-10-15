    void getCookieDomains()
    {
        // Test with empty configuration.
        String configurationKey = "xwiki.authentication.cookiedomains";
        when(this.xwikiCfgConfiguration.getProperty(configurationKey, List.class, List.of()))
            .thenReturn(List.of());

        assertEquals(List.of(), this.configuration.getCookieDomains());

        // Test with domains without prefix.
        when(this.xwikiCfgConfiguration.getProperty(configurationKey, List.class, List.of()))
            .thenReturn(List.of("xwiki.org", "xwiki.com"));

        String xwikiComWithPrefix = ".xwiki.com";
        assertEquals(List.of(".xwiki.org", xwikiComWithPrefix), this.configuration.getCookieDomains());

        // Test with domains where some have a prefix already.
        when(this.xwikiCfgConfiguration.getProperty(configurationKey, List.class, List.of()))
            .thenReturn(List.of("example.com", xwikiComWithPrefix));

        assertEquals(List.of(".example.com", xwikiComWithPrefix), this.configuration.getCookieDomains());
    }