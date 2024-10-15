    void accessDenied()
    {
        when(this.contextualAuthorizationManager.hasAccess(Right.VIEW, ICON_DOCUMENT_REFERENCE)).thenReturn(false);
        this.displayIconMacroParameters.setFallback(false);

        MacroExecutionException executionException = assertThrows(MacroExecutionException.class,
            () -> this.displayIconMacro.execute(this.displayIconMacroParameters, null,
                mock(MacroTransformationContext.class)));
        assertEquals(String.format("Current user [%s] doesn't have view rights on the icon set's document [%s]", null,
            ICON_DOCUMENT_REFERENCE), executionException.getMessage());
    }