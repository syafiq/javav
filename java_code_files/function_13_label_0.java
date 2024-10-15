    void fallbackWhenAccessDenied() throws MacroExecutionException, IconException
    {
        when(this.contextualAuthorizationManager.hasAccess(Right.VIEW, ICON_DOCUMENT_REFERENCE)).thenReturn(false);
        IconSet defaultIconSet = mock(IconSet.class);
        when(this.iconSetManager.getDefaultIconSet()).thenReturn(defaultIconSet);

        List<Block> result =
            this.displayIconMacro.execute(this.displayIconMacroParameters, null, new MacroTransformationContext());
        assertEquals(result, List.of(new MetaDataBlock(List.of(new WordBlock("home")))));
        verify(this.iconRenderer).render("home", defaultIconSet);
        verifyNoInteractions(this.documentContextExecutor);
    }