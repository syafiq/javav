    void doNothingWhenPushThrows() throws Exception
    {
        Exception testException = new Exception("Test");
        doThrow(testException)
            .when(this.documentAccessBridge).pushDocumentInContext(any(), same(this.documentModelBridge));

        assertThrows(Exception.class, () -> this.executor.call(this.mockCallable, this.documentModelBridge));

        verifyNoInteractions(this.mockCallable);
        verify(this.modelContext, never()).setCurrentEntityReference(any());
    }