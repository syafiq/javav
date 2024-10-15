    void existingDocumentFromUITemplateProviderSpecifiedRestrictionExistsOnParentSpace() throws Exception
    {
        // current document = xwiki:Main.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("Main"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(false);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);

        // Submit from the UI spaceReference=X.Y.Z&name=W&templateProvider=XWiki.MyTemplateProvider
        String templateProviderFullName = "XWiki.MyTemplateProvider";
        String spaceReferenceString = "X.Y.Z";
        when(mockRequest.getParameter("spaceReference")).thenReturn(spaceReferenceString);
        when(mockRequest.getParameter("name")).thenReturn("W");
        when(mockRequest.getParameter("templateprovider")).thenReturn(templateProviderFullName);

        // Mock 1 existing template provider that allows usage in one of the target space's parents (top level in this
        // case).
        mockExistingTemplateProviders(templateProviderFullName,
            new DocumentReference("xwiki", Arrays.asList("XWiki"), "MyTemplateProvider"), Arrays.asList("X"));

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note1: We are allowed to create anything under space X or its children, be it a terminal or a non-terminal
        // document
        // Note2: We are creating X.Y.Z.W and using the template extracted from the template provider.
        verify(mockURLFactory).createURL("X.Y.Z.W", "WebHome", "edit",
            "template=XWiki.MyTemplate&parent=Main.WebHome&title=W&form_token=" + CSRF_TOKEN_VALUE,
            null, "xwiki", context);
    }