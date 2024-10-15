    void checkMacrosInjectionWithScriptRights() throws Exception
    {
        when(this.templateContent.getAuthorReference()).thenReturn(SCRIPT_USER);
        VelocityEngine engine = this.velocityManager.getVelocityEngine();
        verify(engine).addGlobalMacros(anyMap());
    }