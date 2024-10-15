    public void setup(MockitoComponentManager componentManager) throws Exception
    {
        when(mockitoOldcore.getSpyXWiki().getDocument(userReference, mockitoOldcore.getXWikiContext()))
            .thenReturn(userDocument);
        when(userDocument.getDocumentReference()).thenReturn(userReference);
        when(userDocument.getDocumentReferenceWithLocale()).thenReturn(userReference);
        when(userDocument.clone()).thenReturn(userDocument);
        componentManager.registerMockComponent(ContextualLocalizationManager.class, "default");
    }