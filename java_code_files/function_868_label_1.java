    void getParametersWithAnEmptyParametersProperty() throws Exception
    {
        when(this.modelContext.getCurrentEntityReference()).thenReturn(new WikiReference("xwiki"));
        WikiUIExtensionParameters parameters = new WikiUIExtensionParameters("id", "", this.componentManager);
        assertEquals(MapUtils.EMPTY_MAP, parameters.get());
    }