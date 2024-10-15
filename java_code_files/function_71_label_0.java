    public void whenSettingTheContextDocumentTheContextWikiIsAlsoSet() throws Exception
    {
        EntityReferenceProvider defaultEntityReferenceProvider = this.mocker.getInstance(EntityReferenceProvider.class);
        when(defaultEntityReferenceProvider.getDefaultReference(EntityType.DOCUMENT)).thenReturn(
            new EntityReference("Page", EntityType.DOCUMENT));

        DocumentModelBridge document = mock(DocumentModelBridge.class);
        DocumentReference documentReference = new DocumentReference("wiki", Arrays.asList("Space"), "Page");
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.getTitle()).thenReturn("title");

        XDOM titleXDOM = new XDOM(Arrays.asList(new WordBlock("title")));

        Parser plainTextParser = this.mocker.getInstance(Parser.class, "plain/1.0");
        when(plainTextParser.parse(any(StringReader.class))).thenReturn(titleXDOM);

        ModelContext modelContext = this.mocker.getInstance(ModelContext.class);
        WikiReference currentWikiReference = new WikiReference("currentWiki");
        when(modelContext.getCurrentEntityReference()).thenReturn(currentWikiReference);

        AuthorizationManager authorizationManager = this.mocker.getInstance(AuthorizationManager.class);
        when(authorizationManager.hasAccess(eq(Right.SCRIPT), any(), any())).thenReturn(true);

        DocumentAccessBridge dab = this.mocker.getInstance(DocumentAccessBridge.class);

        DocumentDisplayerParameters params = new DocumentDisplayerParameters();
        params.setTitleDisplayed(true);
        params.setExecutionContextIsolated(true);

        this.mocker.getComponentUnderTest().display(document, params);

        // Check that the context is set.
        verify(dab).pushDocumentInContext(any(), same(document));
        verify(modelContext).setCurrentEntityReference(documentReference.getWikiReference());

        // Check that the context is restored.
        verify(dab).popDocumentFromContext(any());
        verify(modelContext).setCurrentEntityReference(currentWikiReference);
    }