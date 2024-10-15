    void getParametersFromTheSameRequestAndForTheSameWiki() throws Exception
    {
        when(this.modelContext.getCurrentEntityReference()).thenReturn(new WikiReference("xwiki"));
        when(this.velocityEngine
            .evaluate(any(VelocityContext.class), any(StringWriter.class), eq("id:key"), eq("value")))
            .thenReturn(true);
        WikiUIExtensionParameters parameters = new WikiUIExtensionParameters("id", "key=value", this.componentManager);

        // It should fail silently
        assertEquals("", parameters.get().get("key"));
        assertEquals("", parameters.get().get("key"));

        // Verify the evaluate is done only once
        verify(this.velocityEngine)
            .evaluate(any(VelocityContext.class), any(StringWriter.class), eq("id:key"), eq("value"));
    }