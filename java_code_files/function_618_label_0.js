    void escapeSectionLink() throws Exception
    {
        // Create a new section document.
        XWikiDocument mySectionDoc = new XWikiDocument(MY_SECTION);
        BaseObject object = mySectionDoc.newXObject(CONFIGURABLE_CLASS, this.context);
        object.setStringValue("displayInCategory", "other");
        object.setStringValue("displayInSection", "other");
        object.set("scope", "WIKI+ALL_SPACES", this.context);
        this.xwiki.saveDocument(mySectionDoc, this.context);

        // Make sure the section document is returned by the query and the user has access to edit.
        when(this.query.execute()).thenReturn(List.of(MY_SECTION_SERIALIZED)).thenReturn(List.of());
        when(this.oldcore.getMockRightService()
            .hasAccessLevel(eq("edit"), any(), any(), any())).thenReturn(true);

        // Set a new document with space ">{{/html}}{{noscript /}} as context document to check escaping of the
        // current space.
        String spaceName = "\">{{/html}}{{noscript /}}";
        DocumentReference docRef = new DocumentReference(WIKI_NAME, spaceName, "WebHome");
        XWikiDocument contextDoc = new XWikiDocument(docRef);
        this.xwiki.saveDocument(contextDoc, this.context);
        this.context.setDoc(contextDoc);

        XWikiDocument doc = loadPage(CONFIGURABLE_CLASS);
        Document htmlPage = renderHTMLPage(doc);
        String link = Objects.requireNonNull(htmlPage.selectFirst("li.other a")).attr("href");
        URI uri = new URI(link);
        // Parse the query parameters and check the space name.
        URLEncodedUtils.parse(uri, StandardCharsets.UTF_8).stream()
            .filter(pair -> pair.getName().equals("space"))
            .findFirst()
            .ifPresentOrElse(pair -> assertEquals(spaceName, pair.getValue()), () -> fail("No space parameter in URL"));
    }