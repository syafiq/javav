    void escapesExtensionProperties() throws Exception
    {
        // Create a document with a UI extension to test on.
        DocumentReference docRef = new DocumentReference(WIKI_NAME, XWIKI_SPACE, "Test");
        XWikiDocument document = new XWikiDocument(docRef);
        // Set the author to admin so the UIX can be registered at wiki level.
        document.setAuthorReference(ADMIN_REFERENCE);
        BaseObject uiExtensionObject =
            document.newXObject(WikiUIExtensionConstants.UI_EXTENSION_CLASS, this.context);
        uiExtensionObject.setStringValue(WikiUIExtensionConstants.EXTENSION_POINT_ID_PROPERTY,
            "org.xwiki.platform.search");
        String id = "\")}}{{noscript /}}";
        uiExtensionObject.setStringValue(WikiUIExtensionConstants.ID_PROPERTY, id);
        String label = "Label {{noscript /}}";
        String adminPageName = "\"}}{{noscript /}}Admin";
        String adminPage = XWIKI_SPACE + "." + adminPageName;
        uiExtensionObject.setLargeStringValue(WikiUIExtensionConstants.PARAMETERS_PROPERTY, "label=" + label + "\n"
            + "admin=" + adminPage);
        uiExtensionObject.setStringValue(WikiUIExtensionConstants.SCOPE_PROPERTY, "wiki");
        this.xwiki.saveDocument(document, this.context);

        // The event listeners are not registered by default. We trigger it manually so that the UIX is registered and
        // can be found and rendered.
        this.componentManager
            .<EventListener>getInstance(EventListener.class,
                DefaultWikiObjectComponentManagerEventListener.EVENT_LISTENER_NAME)
            .onEvent(new DocumentCreatedEvent(), document, null);

        // Create the fake search admin page.
        DocumentReference searchAdminReference = new DocumentReference(WIKI_NAME, XWIKI_SPACE, adminPageName);
        XWikiDocument searchAdminDocument = new XWikiDocument(searchAdminReference);
        String searchAdminPageContent = "Search Admin Page Content";
        searchAdminDocument.setContent(searchAdminPageContent);
        this.xwiki.saveDocument(searchAdminDocument, this.context);

        // Load XWiki.SearchCode and XWiki.SearchConfigClass as the SearchAdmin page uses them.
        loadPage(new DocumentReference(WIKI_NAME, XWIKI_SPACE, "SearchCode"));
        loadPage(new DocumentReference(WIKI_NAME, XWIKI_SPACE, "SearchConfigClass"));

        Document htmlPage = renderHTMLPage(SEARCH_ADMIN_SHEET);

        Element optionElement = htmlPage.selectFirst("select option");
        assertNotNull(optionElement);
        assertEquals(id, optionElement.attr("value"));
        assertEquals(label, optionElement.text());

        Element tabLink = htmlPage.selectFirst("ul.nav-tabs li a");
        assertNotNull(tabLink);
        String tabId = String.format("%sConfig", id);
        assertEquals("#" + tabId, tabLink.attr("href"));
        assertEquals(tabId, tabLink.attr("aria-controls"));
        assertEquals(label, tabLink.text());

        Element tabContent = htmlPage.selectFirst("div.tab-content div.tab-pane");
        assertNotNull(tabContent);
        assertEquals(tabId, tabContent.attr("id"));
        assertEquals(searchAdminPageContent, tabContent.text());
    }