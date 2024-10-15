    void popWhenCallableThrows() throws Exception
    {
        Exception testException = new Exception("Callable failed");
        when(this.mockCallable.call()).thenThrow(testException);

        assertThrows(Exception.class, () -> this.executor.call(this.mockCallable, this.documentModelBridge));
        InOrder inOrder = inOrder(this.documentAccessBridge, this.modelContext);
        inOrder.verify(this.modelContext).setCurrentEntityReference(TEST_WIKI_REFERENCE);
        inOrder.verify(this.documentAccessBridge).popDocumentFromContext(any());
        inOrder.verify(this.modelContext).setCurrentEntityReference(INITIAL_WIKI_REFERENCE);
    }