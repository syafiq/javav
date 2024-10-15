    void newDocumentFromURLTemplateProviderSpecifiedNonTerminalButOverriddenFromUITerminal() throws Exception
    {
        // new document = xwiki:X.Y
        DocumentReference documentReference = new DocumentReference("xwiki", "X", "Y");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(true);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);
        String csrfTokenValue = "token42234456";
        when(this.csrfToken.getToken()).thenReturn(csrfTokenValue);

        // Specifying a template provider in the URL: templateprovider=XWiki.MyTemplateProvider
        String templateProviderFullName = "XWiki.MyTemplateProvider";
        when(mockRequest.getParameter("templateprovider")).thenReturn(templateProviderFullName);
        when(mockRequest.getParameter("tocreate")).thenReturn("terminal");

        // Mock 1 existing template provider
        mockExistingTemplateProviders(templateProviderFullName,
            new DocumentReference("xwiki", Arrays.asList("XWiki"), "MyTemplateProvider"), Collections.emptyList(),
            false);

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating the document X.Y as terminal and using a template, as specified in the template
        // provider.
        verify(mockURLFactory).createURL("X", "Y", "edit",
            "template=XWiki.MyTemplate&parent=Main.WebHome&title=Y&form_token=" + csrfTokenValue,
            null, "xwiki", context);
    }