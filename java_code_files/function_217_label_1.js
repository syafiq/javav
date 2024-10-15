    void newDocumentWebHomeFromURLTemplateProviderSpecifiedTerminalOverriddenFromUIToNonTerminal() throws Exception
    {
        // new document = xwiki:X.Y.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("X", "Y"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(true);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);
        String csrfTokenValue = "token4222113555";
        when(this.csrfToken.getToken()).thenReturn(csrfTokenValue);

        // Specifying a template provider in the URL: templateprovider=XWiki.MyTemplateProvider
        String templateProviderFullName = "XWiki.MyTemplateProvider";
        when(mockRequest.getParameter("templateprovider")).thenReturn(templateProviderFullName);
        when(mockRequest.getParameter("tocreate")).thenReturn("nonterminal");

        // Mock 1 existing template provider
        mockExistingTemplateProviders(templateProviderFullName,
            new DocumentReference("xwiki", Arrays.asList("XWiki"), "MyTemplateProvider"), Collections.emptyList(),
            true);

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating the document X.Y.WebHome as non-terminal even if the template provider says otherwise.
        // Also using a template, as specified in the template provider.
        verify(mockURLFactory).createURL("X.Y", "WebHome", "edit",
            "template=XWiki.MyTemplate&parent=Main.WebHome&title=Y&form_token=" + csrfTokenValue,
            null, "xwiki", context);
    }