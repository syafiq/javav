    void viewVelocityCode(boolean allowExecution)
    {
        XWikiDocument spyDocument = getSpyDocument();
        // Velocity code is only supported in XWiki 1.0.
        spyDocument.setSyntax(Syntax.XWIKI_1_0);

        TextAreaClass textAreaClass = new TextAreaClass();
        textAreaClass.setContentType(TextAreaClass.ContentType.VELOCITY_CODE);

        BaseObject object = new BaseObject();
        object.setOwnerDocument(spyDocument);
        String velocityCode = "#set($x = 1) $1 & 1";
        object.setLargeStringValue(PROPERTY_NAME, velocityCode);
        StringBuffer buffer = new StringBuffer();

        when(this.oldcore.getMockContextualAuthorizationManager().hasAccess(Right.SCRIPT)).then(invocationOnMock -> {
            // Verify that the content author is set to the metadata author.
            XWikiDocument sDoc = (XWikiDocument) this.oldcore.getXWikiContext().get(XWikiDocument.CKEY_SDOC);
            assertEquals(GuestUserReference.INSTANCE, sDoc.getAuthors().getContentAuthor());
            return allowExecution;
        });

        String renderingResult = "1 &#38; 1";
        OldRendering oldRendering = mock();
        String renderingInput = velocityCode.replace("&", "&#38;");
        when(oldRendering.parseContent(renderingInput, this.oldcore.getXWikiContext()))
            .thenReturn(renderingResult);
        when(this.oldRenderingProvider.get()).thenReturn(oldRendering);

        textAreaClass.displayView(buffer, PROPERTY_NAME, "", object, true, this.oldcore.getXWikiContext());

        if (allowExecution) {
            assertEquals(renderingResult, buffer.toString());
        } else {
            assertEquals(renderingInput, buffer.toString());
            verify(oldRendering, never()).parseContent(anyString(), any());
        }

        verify(this.oldcore.getMockContextualAuthorizationManager()).hasAccess(Right.SCRIPT);
    }