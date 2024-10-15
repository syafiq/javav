    void loadIconSet() throws Exception
    {
        Reader content = new InputStreamReader(getClass().getResourceAsStream("/test.iconset"));

        // Test
        IconSet result = this.iconSetLoader.loadIconSet(content, "FontAwesome");

        // Verify
        verifies(result);
        assertEquals("FontAwesome", result.getName());
    }