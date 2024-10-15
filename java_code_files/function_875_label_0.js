    void getParametersFromTheSameRequestAndForTheSameWiki() throws Exception
    {
        when(this.velocityEngine
            .evaluate(any(VelocityContext.class), any(StringWriter.class), eq("id:key"), eq("value")))
            .thenReturn(true);
        BaseObject mockUIX = constructMockUIXObject("key=value");
        WikiUIExtensionParameters parameters = new WikiUIExtensionParameters(mockUIX, this.componentManager);

        // It should fail silently
        assertEquals("", parameters.get().get("key"));
        assertEquals("", parameters.get().get("key"));

        // Verify the evaluate is done only once
        verify(this.velocityEngine)
            .evaluate(any(VelocityContext.class), any(StringWriter.class), eq("id:key"), eq("value"));
    }