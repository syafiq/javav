    void getRenderedContentSetsRestrictedRendering() throws Exception
    {
        XWikiDocument otherDocument = new XWikiDocument(new DocumentReference("otherwiki", "otherspace", "otherpage"));
        otherDocument.setContentAuthorReference(new DocumentReference("otherwiki", "XWiki", "othercontentauthor"));
        XWikiDocument sdoc = new XWikiDocument(new DocumentReference("callerwiki", "callerspace", "callerpage"));
        Document apiDocument = this.document.newDocument(this.oldcore.getXWikiContext());

        String content = "{{velocity}}test{{/velocity}}";

        this.document.setRestricted(true);
        this.document.setContent(content);
        this.oldcore.getXWikiContext().setDoc(null);

        // Verify that the Velocity macro is not executed.
        assertThat(this.document.getRenderedContent(this.oldcore.getXWikiContext()),
            startsWith("<div class=\"xwikirenderingerror\">Failed to execute the [velocity] macro."));

        this.document.setRestricted(false);

        assertEquals("<p>test</p>", this.document.getRenderedContent(this.oldcore.getXWikiContext()));

        this.oldcore.getXWikiContext().setDoc(otherDocument);

        assertEquals("<p>test</p>", apiDocument.getRenderedContent(content, Syntax.XWIKI_2_1.toIdString()));

        otherDocument.setRestricted(true);

        assertThat(apiDocument.getRenderedContent(content, Syntax.XWIKI_2_1.toIdString()),
            startsWith("<div class=\"xwikirenderingerror\">Failed to execute the [velocity] macro."));

        this.oldcore.getXWikiContext().put("sdoc", sdoc);
        assertEquals("<p>test</p>", apiDocument.getRenderedContent(content, Syntax.XWIKI_2_1.toIdString()));

        sdoc.setRestricted(true);

        assertThat(apiDocument.getRenderedContent(content, Syntax.XWIKI_2_1.toIdString()),
            startsWith("<div class=\"xwikirenderingerror\">Failed to execute the [velocity] macro."));
    }