    public void loadIconSetFromWikiDocument() throws Exception
    {
        DocumentReference iconSetRef = new DocumentReference("xwiki", "IconThemes", "Default");
        DocumentReference iconClassRef = new DocumentReference("wikiId", "IconThemesCode", "IconThemeClass");
        when(documentAccessBridge.getProperty(eq(iconSetRef), eq(iconClassRef), eq("name"))).thenReturn("MyIconTheme");
        DocumentModelBridge doc = mock(DocumentModelBridge.class);
        when(documentAccessBridge.getDocumentInstance(iconSetRef)).thenReturn(doc);

        StringWriter content = new StringWriter();
        IOUtils.copyLarge(new InputStreamReader(getClass().getResourceAsStream("/test.iconset")), content);
        when(doc.getContent()).thenReturn(content.toString());

        // Test
        IconSet result = iconSetLoader.loadIconSet(iconSetRef);

        // Verify
        verifies(result);
        assertEquals("MyIconTheme", result.getName());
    }