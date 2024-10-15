    void escapeHeadingForError() throws Exception
    {
        this.request.put("section", "other");
        when(this.query.execute()).thenReturn(List.of(MY_SECTION_SERIALIZED)).thenReturn(List.of());

        XWikiDocument mySectionDoc = new XWikiDocument(MY_SECTION);
        this.xwiki.saveDocument(mySectionDoc, this.context);

        Document htmlPage = renderHTMLPage(CONFIGURABLE_CLASS);
        assertEquals(String.format("admin.customize %s:", MY_SECTION_SERIALIZED),
            htmlPage.selectFirst("h1").text());
    }