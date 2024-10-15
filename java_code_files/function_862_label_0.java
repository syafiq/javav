    void getParametersWithAnEqualSignInAValue() throws Exception
    {
        when(this.velocityEngine
            .evaluate(any(VelocityContext.class), any(StringWriter.class), eq("id:key"), eq("value")))
            .thenReturn(true);
        BaseObject mockUIX = constructMockUIXObject("key=value");
        WikiUIExtensionParameters parameters = new WikiUIExtensionParameters(mockUIX, this.componentManager);

        // Since the StringWriter is created within the method, the value is "" and not "value".
        assertEquals("", parameters.get().get("key"));
    }