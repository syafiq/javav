    void executesInContext() throws Exception
    {
        when(this.contextualAuthorizationManager.hasAccess(Right.VIEW, ICON_DOCUMENT_REFERENCE)).thenReturn(true);

        List<Block> result =
            this.displayIconMacro.execute(this.displayIconMacroParameters, null, new MacroTransformationContext());
        assertEquals(result, List.of(new MetaDataBlock(List.of(new WordBlock("home")))));
        verify(this.documentContextExecutor).call(any(), eq(this.iconDocument));
    }