    void renderWithContextDocument() throws Exception
    {
        // Mocks
        VelocityEngine engine = mock(VelocityEngine.class);
        when(this.velocityManager.getVelocityEngine()).thenReturn(engine);
        when(engine.evaluate(any(VelocityContext.class), any(Writer.class), any(), eq("myCode"))).thenAnswer(
            invocation -> {
                // Get the writer
                Writer writer = (Writer) invocation.getArguments()[1];
                writer.write("Rendered code");
                return true;
            });

        DocumentReference contextReference = new DocumentReference("xwiki", "Space", "IconTheme");
        DocumentReference documentAuthorReference = new DocumentReference("xwiki", "XWiki", "User");
        XWikiDocument document = mock(XWikiDocument.class);
        UserReference authorReference = mock(UserReference.class);
        DocumentAuthors documentAuthors = new DefaultDocumentAuthors(document);
        documentAuthors.setContentAuthor(authorReference);
        when(document.getAuthors()).thenReturn(documentAuthors);
        when(this.documentUserSerializer.serialize(authorReference)).thenReturn(documentAuthorReference);
        when(this.documentAccessBridge.getDocumentInstance(contextReference)).thenReturn(document);
        when(this.authorExecutor.call(any(), eq(documentAuthorReference), eq(contextReference)))
            .then(invocation -> invocation.getArgument(0, Callable.class).call());
        when(this.documentContextExecutor.call(any(), eq(document)))
            .then(invocation -> invocation.getArgument(0, Callable.class).call());

        assertEquals("Rendered code", this.velocityRenderer.render("myCode", contextReference));
    }