    void getParametersWithoutScriptRight() throws Exception
    {
        when(this.authorizationManager.hasAccess(Right.SCRIPT, AUTHOR_REFERENCE, DOCUMENT_REFERENCE)).thenReturn(false);
        BaseObject mockUIX = constructMockUIXObject("key=value");
        WikiUIExtensionParameters parameters = new WikiUIExtensionParameters(mockUIX, this.componentManager);

        assertEquals("value", parameters.get().get("key"));
        verifyNoInteractions(this.velocityEngine);
    }