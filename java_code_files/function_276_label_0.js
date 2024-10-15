    void viewWikiText()
    {
        // Use a spy, so we don't need to mess around with clone-support in a mock.
        XWikiDocument spyDocument = getSpyDocument();

        TextAreaClass textAreaClass = new TextAreaClass();
        textAreaClass.setContentType(TextAreaClass.ContentType.WIKI_TEXT);
        BaseObject object = new BaseObject();
        object.setOwnerDocument(spyDocument);
        object.setLargeStringValue(PROPERTY_NAME, "**Test bold**");
        StringBuffer buffer = new StringBuffer();
        String renderingResult = "<p><strong>Test bold</strong></p>";
        doAnswer(invocationOnMock -> {
            XWikiDocument sDoc = invocationOnMock.getArgument(3);
            // Verify that the content author is set to the metadata author.
            assertEquals(GuestUserReference.INSTANCE, sDoc.getAuthors().getContentAuthor());
            return renderingResult;
        }).when(spyDocument).getRenderedContent(anyString(), any(Syntax.class), anyBoolean(), any(XWikiDocument.class),
            anyBoolean(), any(XWikiContext.class));
        textAreaClass.displayView(buffer, PROPERTY_NAME, "", object, true, this.oldcore.getXWikiContext());

        verify(spyDocument).getRenderedContent(anyString(), any(Syntax.class), anyBoolean(),
            any(XWikiDocument.class), anyBoolean(), any(XWikiContext.class));

        assertEquals(renderingResult, buffer.toString());
    }