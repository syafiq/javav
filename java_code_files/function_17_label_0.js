    void getIconSetWhenOneFails() throws Exception
    {
        // Mocks
        IconSet iconSet = new IconSet("silk");
        Query query = mock(Query.class);
        when(this.queryManager.createQuery("FROM doc.object(IconThemesCode.IconThemeClass) obj WHERE obj.name = :name",
            Query.XWQL)).thenReturn(query);
        List<String> results = List.of("FakeIcon.Silk", "IconThemes.Silk");
        when(query.<String>execute()).thenReturn(results);
        DocumentReference fakeDocumentReference = new DocumentReference("wiki", "FakeIcon", "Silk");
        when(this.documentReferenceResolver.resolve("FakeIcon.Silk")).thenReturn(fakeDocumentReference);
        when(this.iconSetLoader.loadIconSet(fakeDocumentReference)).thenThrow(new IconException("Test"));

        DocumentReference documentReference = new DocumentReference("wiki", "IconThemes", "Silk");
        when(this.documentReferenceResolver.resolve("IconThemes.Silk")).thenReturn(documentReference);
        when(this.iconSetLoader.loadIconSet(documentReference)).thenReturn(iconSet);

        // Test
        assertEquals(iconSet, this.iconSetManager.getIconSet("silk"));

        // Verify
        verify(query).bindValue("name", "silk");
        verify(this.iconSetCache).put(documentReference, iconSet);
        verify(this.iconSetCache).put("silk", "currentWikiId", iconSet);
        verify(this.iconSetLoader).loadIconSet(fakeDocumentReference);
    }