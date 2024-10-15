    void checkScriptRight(boolean hasScript) throws Exception
    {
        this.request.put("section", "other");
        when(this.query.execute()).thenReturn(List.of(MY_SECTION_SERIALIZED)).thenReturn(List.of());
        when(this.oldcore.getMockRightService()
            .hasAccessLevel(eq("edit"), any(), any(), any())).thenReturn(true);

        XWikiDocument mySectionDoc = new XWikiDocument(MY_SECTION);
        BaseObject object = mySectionDoc.newXObject(CONFIGURABLE_CLASS, this.context);
        object.setStringValue("displayInCategory", "other");
        object.setStringValue("displayInSection", "other");
        String originalHeading = "$appName {{noscript /}}";
        object.setStringValue("heading", originalHeading);
        object.set("scope", "WIKI+ALL_SPACES", this.context);
        DocumentReference userReference = new DocumentReference(WIKI_NAME, SPACE_NAME, "Admin");
        mySectionDoc.getAuthors().setEffectiveMetadataAuthor(new DocumentUserReference(userReference, true));
        this.xwiki.saveDocument(mySectionDoc, this.context);
        when(this.oldcore.getMockAuthorizationManager().hasAccess(Right.SCRIPT,
            userReference, mySectionDoc.getDocumentReference())).thenReturn(hasScript);

        Document htmlPage = renderHTMLPage(CONFIGURABLE_CLASS);
        String expected;
        if (hasScript) {
            expected = String.format("%s {{noscript /}}", MY_SECTION_SERIALIZED);
        } else {
            expected = originalHeading;
        }
        assertEquals(expected, htmlPage.selectFirst("h2").text());
    }