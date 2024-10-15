    void existingDocumentFromUITemplateProviderSpecifiedTerminalOverridenFromUIToNonTerminal() throws Exception
    {
        // current document = xwiki:Main.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("Main"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(false);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);

        // Submit from the UI spaceReference=X&name=Y&templateProvider=XWiki.MyTemplateProvider
        String templateProviderFullName = "XWiki.MyTemplateProvider";
        String spaceReferenceString = "X";
        when(mockRequest.getParameter("spaceReference")).thenReturn(spaceReferenceString);
        when(mockRequest.getParameter("name")).thenReturn("Y");
        when(mockRequest.getParameter("templateprovider")).thenReturn(templateProviderFullName);
        when(mockRequest.getParameter("tocreate")).thenReturn("nonterminal");

        // Mock 1 existing template provider that creates terminal documents.
        mockExistingTemplateProviders(templateProviderFullName,
            new DocumentReference("xwiki", Arrays.asList("XWiki"), "MyTemplateProvider"), Collections.emptyList(),
            true);

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating the document X.Y.WebHome as non-terminal, even if the template provider says otherwise.
        // Also using a template, as specified in the template provider.
        verify(mockURLFactory).createURL("X.Y", "WebHome", "edit",
            "template=XWiki.MyTemplate&parent=Main.WebHome&title=Y&form_token=" + CSRF_TOKEN_VALUE,
            null, "xwiki", context);
    }