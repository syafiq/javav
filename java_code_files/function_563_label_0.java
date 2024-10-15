    void dataURIIsKept() throws DiffException
    {
        String dataURI = "data:image/png;base64,abc";
        assertEquals(dataURI, this.converter.convert(dataURI));
    }