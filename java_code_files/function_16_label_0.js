    void getIconSetWhenAllFail() throws Exception
    {
        // Mocks
        Query query = mock(Query.class);
        when(this.queryManager.createQuery("FROM doc.object(IconThemesCode.IconThemeClass) obj WHERE obj.name = :name",
            Query.XWQL)).thenReturn(query);
        List<String> results = List.of("FakeIcon.Silk", "IconThemes.Silk");
        when(query.<String>execute()).thenReturn(results);
        DocumentReference fakeDocumentReference = new DocumentReference("wiki", "FakeIcon", "Silk");
        when(this.documentReferenceResolver.resolve("FakeIcon.Silk")).thenReturn(fakeDocumentReference);
        IconException fakeException = new IconException("Fake");
        when(this.iconSetLoader.loadIconSet(fakeDocumentReference)).thenThrow(fakeException);

        DocumentReference documentReference = new DocumentReference("wiki", "IconThemes", "Silk");
        when(this.documentReferenceResolver.resolve("IconThemes.Silk")).thenReturn(documentReference);
        when(this.iconSetLoader.loadIconSet(documentReference)).thenThrow(new IconException("Real"));

        // Test
        IconException exception = assertThrows(IconException.class, () -> this.iconSetManager.getIconSet("silk"));
        assertEquals("Failed to load the icon set [silk] from 2 documents, reporting the first exception, see the"
            + " log for additional errors.", exception.getMessage());
        assertEquals(fakeException, exception.getCause());

        assertEquals(1, this.logCapture.size());
        assertEquals("Failed loading icon set [silk] from multiple matching documents, "
                + "ignored this additional exception, reason: [IconException: Real].",
            this.logCapture.getMessage(0));

        // Verify
        verify(query).bindValue("name", "silk");
        verify(this.iconSetCache, never()).put(anyString(), any());
        verify(this.iconSetCache, never()).put(any(DocumentReference.class), any());
        verify(this.iconSetLoader).loadIconSet(fakeDocumentReference);
    }