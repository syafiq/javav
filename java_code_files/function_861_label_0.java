    void getParametersWithAuthorExecutor() throws Exception
    {
        // Do not call the callable to check that the call to the Velocity engine is inside the author executor.
        doReturn(null).when(this.authorExecutor).call(any(), any(), any());

        BaseObject mockUIX = constructMockUIXObject("key=value");
        WikiUIExtensionParameters parameters = new WikiUIExtensionParameters(mockUIX, this.componentManager);

        assertEquals("value", parameters.get().get("key"));
        verify(this.authorExecutor).call(any(), eq(AUTHOR_REFERENCE), eq(DOCUMENT_REFERENCE));
        verifyNoInteractions(this.velocityEngine);
    }