    void getParametersWithAnEqualSignInAValue() throws Exception
    {
        when(this.modelContext.getCurrentEntityReference()).thenReturn(new WikiReference("xwiki"));
        when(this.velocityEngine
            .evaluate(any(VelocityContext.class), any(StringWriter.class), eq("id:key"), eq("value")))
            .thenReturn(true);
        WikiUIExtensionParameters parameters = new WikiUIExtensionParameters("id", "key=value", this.componentManager);

        // Since the StringWriter is created within the method, the value is "" and not "value".
        assertEquals("", parameters.get().get("key"));
    }