    void getParametersWhenVelocityFails() throws Exception
    {
        when(this.velocityEngine
            .evaluate(any(VelocityContext.class), any(StringWriter.class), eq("id:key"), eq("value")))
            .thenThrow(new XWikiVelocityException(""));
        BaseObject mockUIX = constructMockUIXObject("key=value");
        WikiUIExtensionParameters parameters = new WikiUIExtensionParameters(mockUIX, this.componentManager);

        // It put a warning in the logs and return the not-evaluated value.
        assertEquals("value", parameters.get().get("key"));
        assertEquals("Failed to evaluate UI extension data value, key [key], value [value]. Reason: []",
            this.logCapture.getMessage(0));
    }