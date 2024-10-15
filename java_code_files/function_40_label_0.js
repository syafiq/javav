    void loadIconSetFromWikiDocument() throws Exception
    {
        DocumentReference iconSetRef = new DocumentReference("xwiki", "IconThemes", "Default");
        DocumentReference iconClassRef = new DocumentReference("wikiId", "IconThemesCode", "IconThemeClass");
        when(this.documentAccessBridge.getProperty(iconSetRef, iconClassRef, "name")).thenReturn("MyIconTheme");
        DocumentModelBridge doc = mock(DocumentModelBridge.class);
        when(this.documentAccessBridge.getDocumentInstance(iconSetRef)).thenReturn(doc);

        StringWriter content = new StringWriter();
        IOUtils.copyLarge(new InputStreamReader(getClass().getResourceAsStream("/test.iconset")), content);
        when(doc.getContent()).thenReturn(content.toString());

        when(doc.getAuthors()).thenReturn(new DefaultDocumentAuthors(new XWikiDocument(iconSetRef)));

        // Test
        IconSet result = this.iconSetLoader.loadIconSet(iconSetRef);

        // Verify
        verifies(result);
        assertEquals("MyIconTheme", result.getName());
    }