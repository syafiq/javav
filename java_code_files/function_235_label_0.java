    void existingDocumentFromUITemplateProviderSpecifiedButOldSpaceTypeButOverridenFromUIToTerminal() throws Exception
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
        when(mockRequest.getParameter("tocreate")).thenReturn("terminal");

        // Mock 1 existing template provider
        mockExistingTemplateProviders(templateProviderFullName,
            new DocumentReference("xwiki", Arrays.asList("XWiki"), "MyTemplateProvider"), Collections.emptyList(), null,
            "space");

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating X.Y as terminal, since it is overriden from the UI, regardless of any backwards
        // compatibility resolutions. Also using the template extracted from the template provider.
        verify(mockURLFactory).createURL("X", "Y", "edit",
            "template=XWiki.MyTemplate&parent=Main.WebHome&title=Y&form_token=" + CSRF_TOKEN_VALUE,
            null, "xwiki", context);
    }