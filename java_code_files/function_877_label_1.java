    void getParametersFromTheSameRequestButForDifferentWikis() throws Exception
    {
        when(this.modelContext.getCurrentEntityReference()).thenReturn(new WikiReference("wiki1"))
            .thenReturn(new WikiReference("wiki2"));
        when(this.velocityEngine
            .evaluate(any(VelocityContext.class), any(StringWriter.class), eq("id:key"), eq("value")))
            .thenReturn(true);
        WikiUIExtensionParameters parameters = new WikiUIExtensionParameters("id", "key=value", this.componentManager);

        // It should fail silently
        assertEquals("", parameters.get().get("key"));
        assertEquals("", parameters.get().get("key"));

        // Verify the velocity evaluation has been done for both wikis.
        verify(this.velocityEngine, times(2))
            .evaluate(any(VelocityContext.class), any(StringWriter.class), eq("id:key"), eq("value"));
    }