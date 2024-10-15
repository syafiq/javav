    public void setup(MockitoOldcore mockitoOldcore) throws XWikiException
    {
        XWikiContext xWikiContext = mockitoOldcore.getXWikiContext();
        this.apiXWiki = new XWiki(mockitoOldcore.getSpyXWiki(), xWikiContext);
        XWikiRightService mockRightService = mockitoOldcore.getMockRightService();
        when(mockRightService.hasProgrammingRights(any(), any())).thenReturn(true);
        when(mockRightService.hasProgrammingRights(any())).thenReturn(true);
        when(mockRightService.hasAccessLevel(any(), any(), any(), any())).thenReturn(true);
        when(mockitoOldcore.getMockVersioningStore().getXWikiDocumentArchive(any(), any()))
            .thenReturn(new XWikiDocumentArchive());

        xWikiContext.setUser("Redtail");
        this.apiDocument = new Document(new XWikiDocument(DOCUMENT_REFERENCE), xWikiContext);
        this.apiDocument.getDocument().setCreator("c" + xWikiContext.getUser());
        this.apiDocument.getDocument().setAuthor("a" + xWikiContext.getUser());
        this.apiDocument.save();
        xWikiContext.setUser("Earth");
    }