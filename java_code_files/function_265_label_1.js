    void existingDocumentFromUITemplateSpecified() throws Exception
    {
        // current document = xwiki:Main.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("Main"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(false);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);

        context.setDoc(document);
        String csrfTokenValue = "token4298833";
        when(this.csrfToken.getToken()).thenReturn(csrfTokenValue);

        // Submit from the UI spaceReference=X&name=Y&template=XWiki.MyTemplate
        String templateDocumentFullName = "XWiki.MyTemplate";
        DocumentReference templateDocumentReference =
            new DocumentReference("MyTemplate", Arrays.asList("XWiki"), "xwiki");
        when(mockRequest.getParameter("spaceReference")).thenReturn("X");
        when(mockRequest.getParameter("name")).thenReturn("Y");
        when(mockRequest.getParameter("template")).thenReturn("XWiki.MyTemplate");

        // Mock the passed template document as existing.
        mockTemplateDocumentExisting(templateDocumentFullName, templateDocumentReference);

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating X.Y.WebHome and using the template specified in the request.
        verify(mockURLFactory).createURL("X.Y", "WebHome", "edit",
            "template=XWiki.MyTemplate&parent=Main.WebHome&title=Y&form_token=" + csrfTokenValue,
            null, "xwiki", context);
    }