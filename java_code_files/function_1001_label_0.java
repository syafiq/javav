    public void setup(MockitoComponentManager componentManager) throws Exception
    {
        when(this.mockitoOldcore.getSpyXWiki().getDocument(this.userReference, this.mockitoOldcore.getXWikiContext()))
            .thenReturn(this.userDocument);
        when(this.userDocument.getDocumentReference()).thenReturn(this.userReference);
        when(this.userDocument.getDocumentReferenceWithLocale()).thenReturn(this.userReference);
        when(this.userDocument.getAuthors()).thenReturn(this.authors);
        when(this.userDocument.clone()).thenReturn(this.userDocument);
        componentManager.registerMockComponent(ContextualLocalizationManager.class, "default");
    }