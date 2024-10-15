    void returnsURLWhenDisabled() throws DiffException
    {
        when(this.configuration.isEnabled()).thenReturn(false);
        String url = "http://www.example.com";
        assertEquals(url, this.converter.convert(url));
    }