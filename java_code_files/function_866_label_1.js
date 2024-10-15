    void getParametersWhenVelocityFails() throws Exception
    {
        when(this.modelContext.getCurrentEntityReference()).thenReturn(new WikiReference("xwiki"));
        when(this.velocityEngine
            .evaluate(any(VelocityContext.class), any(StringWriter.class), eq("id:key"), eq("value")))
            .thenThrow(new XWikiVelocityException(""));
        WikiUIExtensionParameters parameters = new WikiUIExtensionParameters("id", "key=value", this.componentManager);

        // It should fail and put a warn in the logs
        assertNull(parameters.get().get("key"));
        assertEquals("Failed to evaluate UI extension data value, key [key], value [value]. Reason: []",
            this.logCapture.getMessage(0));
    }