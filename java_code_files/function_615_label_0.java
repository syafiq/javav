    void escapeHeading() throws Exception
    {
        this.request.put("section", "other");
        when(this.query.execute()).thenReturn(List.of(MY_SECTION_SERIALIZED)).thenReturn(List.of());
        when(this.oldcore.getMockRightService()
            .hasAccessLevel(eq("edit"), any(), any(), any())).thenReturn(true);

        XWikiDocument mySectionDoc = new XWikiDocument(MY_SECTION);
        BaseObject object = mySectionDoc.newXObject(CONFIGURABLE_CLASS, this.context);
        object.setStringValue("displayInCategory", "other");
        object.setStringValue("displayInSection", "other");
        object.setStringValue("heading", "{{noscript /}}");
        object.set("scope", "WIKI+ALL_SPACES", this.context);
        this.xwiki.saveDocument(mySectionDoc, this.context);

        Document htmlPage = renderHTMLPage(CONFIGURABLE_CLASS);
        assertEquals("{{noscript /}}", htmlPage.selectFirst("h2").text());
    }