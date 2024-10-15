    void testDocumentAlreadyExistsError() throws Exception
    {
        // Set "createException" to an XWikiException to simulate a document exists already error.
        String urlToDocument = "space/%3C%2Fdiv%3Epage";
        Object[] args = { DOCUMENT_REFERENCE };
        XWikiException documentAlreadyExistsException = new XWikiException(XWikiException.MODULE_XWIKI_STORE,
            XWikiException.ERROR_XWIKI_APP_DOCUMENT_NOT_EMPTY,
            "Cannot create document {0} because it already has content", null, args);
        this.velocityManager.getVelocityContext().put(CREATE_EXCEPTION_VELOCITY_KEY, documentAlreadyExistsException);
        this.velocityManager.getVelocityContext().put("existingDocumentReference", DOCUMENT_REFERENCE);

        // Render the template.
        Document document = Jsoup.parse(this.templateManager.render(CREATE_INLINE_VM));
        Element errormessage = document.getElementsByClass(ERROR_MESSAGE_CLASS).first();
        assertNotNull(errormessage);
        String viewURL = String.format("/xwiki/bin/view/%s", urlToDocument);
        String editURL = String.format("/xwiki/bin/edit/%s", urlToDocument);
        String expectedMessage = String.format("core.create.page.error.docalreadyexists [%s, %s, %s]",
            DOCUMENT_REFERENCE, viewURL, editURL);
        assertEquals(expectedMessage, errormessage.text());
    }