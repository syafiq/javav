    void existingDocumentFromUITemplateProviderSpecifiedButOldSpaceType() throws Exception
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
        when(mockRequest.getParameter("spaceReference")).thenReturn("X");
        when(mockRequest.getParameter("name")).thenReturn("Y");
        when(mockRequest.getParameter("templateprovider")).thenReturn(templateProviderFullName);

        // Mock 1 existing template provider
        mockExistingTemplateProviders(templateProviderFullName,
            new DocumentReference("xwiki", Arrays.asList("XWiki"), "MyTemplateProvider"), Collections.emptyList(), null,
            "space");

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating X.Y.WebHome as non-terminal, since the template provider does not specify a "terminal"
        // property and we fallback on the "type" property's value. Also using the template extracted from the template
        // provider.
        verify(mockURLFactory).createURL("X.Y", "WebHome", "edit",
            "template=XWiki.MyTemplate&parent=Main.WebHome&title=Y&form_token=" + CSRF_TOKEN_VALUE,
            null, "xwiki", context);
    }