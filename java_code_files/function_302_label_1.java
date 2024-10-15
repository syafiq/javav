    void testNameValidationError() throws Exception
    {
        // Set "createException" to an XWikiException to simulate a validation error.
        String documentReference = "xwiki:space.</div>page";
        Object[] args = { documentReference };
        XWikiException invalidNameException = new XWikiException(XWikiException.MODULE_XWIKI_STORE,
            XWikiException.ERROR_XWIKI_APP_DOCUMENT_NAME_INVALID,
            "Cannot create document {0} because its name does not respect the name strategy of the wiki.", null,
            args);
        this.velocityManager.getVelocityContext().put("createException", invalidNameException);
        this.velocityManager.getVelocityContext().put("invalidNameReference", documentReference);

        // Render the template.
        Document document = Jsoup.parse(this.templateManager.render(CREATE_INLINE_VM));
        Element errormessage = document.getElementsByClass("errormessage").first();
        assertNotNull(errormessage);
        String expectedMessage = String.format("entitynamevalidation.create.invalidname [%s]", documentReference);
        assertEquals(expectedMessage, errormessage.text());
    }