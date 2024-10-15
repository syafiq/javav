    void testNameValidationError() throws Exception
    {
        // Set "createException" to an XWikiException to simulate a validation error.
        Object[] args = { DOCUMENT_REFERENCE };
        XWikiException invalidNameException = new XWikiException(XWikiException.MODULE_XWIKI_STORE,
            XWikiException.ERROR_XWIKI_APP_DOCUMENT_NAME_INVALID,
            "Cannot create document {0} because its name does not respect the name strategy of the wiki.", null,
            args);
        this.velocityManager.getVelocityContext().put(CREATE_EXCEPTION_VELOCITY_KEY, invalidNameException);
        this.velocityManager.getVelocityContext().put("invalidNameReference", DOCUMENT_REFERENCE);

        // Render the template.
        Document document = Jsoup.parse(this.templateManager.render(CREATE_INLINE_VM));
        Element errormessage = document.getElementsByClass(ERROR_MESSAGE_CLASS).first();
        assertNotNull(errormessage);
        String expectedMessage = String.format("entitynamevalidation.create.invalidname [%s]", DOCUMENT_REFERENCE);
        assertEquals(expectedMessage, errormessage.text());
    }