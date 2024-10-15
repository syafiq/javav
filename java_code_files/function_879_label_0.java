    void getParametersWithEmptyKey() throws Exception
    {
        BaseObject mockUIX = constructMockUIXObject("=value");
        WikiUIExtensionParameters parameters = new WikiUIExtensionParameters(mockUIX, this.componentManager);

        assertTrue(parameters.get().isEmpty());
    }