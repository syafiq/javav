    void escapeNonViewableSections() throws Exception
    {
        // Create a new section document.
        XWikiDocument mySectionDoc = new XWikiDocument(MY_SECTION);
        this.xwiki.saveDocument(mySectionDoc, this.context);

        when(this.oldcore.getMockRightService()
            .hasAccessLevel(eq("view"), any(), eq("xwiki:" + MY_SECTION_SERIALIZED), any())).thenReturn(false);

        // Make sure the section document is returned by the query.
        when(this.query.execute()).thenReturn(List.of(MY_SECTION_SERIALIZED)).thenReturn(List.of());

        DocumentReference docRef = new DocumentReference(WIKI_NAME, "\">{{/html}}{{noscript /}}", "WebHome");
        XWikiDocument contextDoc = new XWikiDocument(docRef);
        this.xwiki.saveDocument(contextDoc, this.context);
        this.context.setDoc(contextDoc);

        XWikiDocument doc = loadPage(CONFIGURABLE_CLASS);
        Document htmlPage = renderHTMLPage(doc);
        String errorMessage = Objects.requireNonNull(htmlPage.selectFirst("div.errormessage p")).text();
        assertEquals(String.format("xe.admin.configurable.noViewAccessSomeApplications [[%s]]", MY_SECTION_SERIALIZED),
            errorMessage);
    }