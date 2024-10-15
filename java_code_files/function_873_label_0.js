    void getParametersFromDifferentRequests() throws Exception
    {
        when(this.velocityEngine
            .evaluate(any(VelocityContext.class), any(StringWriter.class), eq("id:key"), eq("value")))
            .thenReturn(true);
        BaseObject mockUIX = constructMockUIXObject("key=value");
        WikiUIExtensionParameters parameters = new WikiUIExtensionParameters(mockUIX, this.componentManager);

        ExecutionContext ec1 = mock(ExecutionContext.class, "ec1");
        ExecutionContext ec2 = mock(ExecutionContext.class, "ec2");
        when(this.execution.getContext()).thenReturn(ec1).thenReturn(ec2);

        // It should fail silently
        assertEquals("", parameters.get().get("key"));
        assertEquals("", parameters.get().get("key"));

        // Verify the velocity evaluation has been done for both wikis.
        verify(this.velocityEngine, times(2))
            .evaluate(any(VelocityContext.class), any(StringWriter.class), eq("id:key"), eq("value"));
    }