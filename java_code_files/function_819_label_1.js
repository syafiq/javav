    public void rollbackFiresEvents() throws Exception
    {
        ObservationManager observationManager = this.componentManager.getInstance(ObservationManager.class);

        XWikiDocument originalDocument = mock(XWikiDocument.class);
        // Mark the document as existing so that the roll-back method will fire an update event.
        when(originalDocument.isNew()).thenReturn(false);

        DocumentReference documentReference = new DocumentReference("wiki", "Space", "Page");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.clone()).thenReturn(document);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.getOriginalDocument()).thenReturn(originalDocument);

        XWikiDocument result = mock(XWikiDocument.class);
        when(result.getDocumentReference()).thenReturn(documentReference);

        String revision = "3.5";
        when(this.documentRevisionProvider.getRevision(document, revision)).thenReturn(result);

        this.componentManager.registerMockComponent(ContextualLocalizationManager.class);

        xwiki.rollback(document, revision, context);

        verify(observationManager).notify(new DocumentRollingBackEvent(documentReference, revision), document, context);
        verify(observationManager).notify(new DocumentUpdatingEvent(documentReference), document, context);
        verify(observationManager).notify(new DocumentUpdatedEvent(documentReference), document, context);
        verify(observationManager).notify(new DocumentRolledBackEvent(documentReference, revision), document, context);
    }