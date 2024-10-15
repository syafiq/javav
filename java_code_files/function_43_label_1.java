    public void loadIconSetWithException() throws Exception
    {
        Reader content = mock(Reader.class);
        IOException exception = new IOException("test");
        when(content.read(any(char[].class))).thenThrow(exception);

        // Test
        Exception caughException = null;
        try {
            iconSetLoader.loadIconSet(content, "FontAwesome");
        } catch (IconException e) {
            caughException = e;
        }

        assertNotNull(caughException);
        assert (caughException instanceof IconException);
        assertEquals(exception, caughException.getCause());
        assertEquals("Failed to load the IconSet [FontAwesome].", caughException.getMessage());
    }