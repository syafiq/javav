    void loadIconSetFromWikiDocumentWithException() throws Exception
    {
        Exception exception = new Exception("test");
        when(this.documentAccessBridge.getDocumentInstance(any(DocumentReference.class))).thenThrow(exception);

        IconException caughtException = assertThrows(IconException.class, () ->
            this.iconSetLoader.loadIconSet(new DocumentReference("a", "b", "c")));

        assertEquals(exception, caughtException.getCause());
        assertEquals("Failed to load the IconSet [a:b.c].", caughtException.getMessage());
    }