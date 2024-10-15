    public void loadIconSetFromWikiDocumentWithException() throws Exception
    {
        Exception exception = new Exception("test");
        when(documentAccessBridge.getDocumentInstance(any(DocumentReference.class))).thenThrow(exception);

        // Test
        Exception caughException = null;
        try {
            iconSetLoader.loadIconSet(new DocumentReference("a", "b", "c"));
        } catch (IconException e) {
            caughException = e;
        }

        assertNotNull(caughException);
        assert (caughException instanceof IconException);
        assertEquals(exception, caughException.getCause());
        assertEquals("Failed to load the IconSet [a:b.c].", caughException.getMessage());
    }