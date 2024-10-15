    void existingDocumentFromUITemplateProviderSpecifiedNonTerminal() throws Exception
    {
        // current document = xwiki:Main.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("Main"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(false);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);
        String csrfTokenValue = "token424111553";
        when(this.csrfToken.getToken()).thenReturn(csrfTokenValue);

        // Submit from the UI spaceReference=X&name=Y&templateProvider=XWiki.MyTemplateProvider
        String templateProviderFullName = "XWiki.MyTemplateProvider";
        String spaceReferenceString = "X";
        when(mockRequest.getParameter("spaceReference")).thenReturn(spaceReferenceString);
        when(mockRequest.getParameter("name")).thenReturn("Y");
        when(mockRequest.getParameter("templateprovider")).thenReturn(templateProviderFullName);

        // Mock 1 existing template provider that creates terminal documents.
        mockExistingTemplateProviders(templateProviderFullName,
            new DocumentReference("xwiki", Arrays.asList("XWiki"), "MyTemplateProvider"), Collections.emptyList(),
            false);

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating the document X.Y.WebHome as non-terminal and using a template, as specified in the
        // template provider.
        verify(mockURLFactory).createURL("X.Y", "WebHome", "edit",
            "template=XWiki.MyTemplate&parent=Main.WebHome&title=Y&form_token=" + csrfTokenValue,
            null, "xwiki", context);
    }