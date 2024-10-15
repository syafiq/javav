    void existingDocumentTerminalFromUI() throws Exception
    {
        // current document = xwiki:Main.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("Main"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(false);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);
        String csrfTokenValue = "token424334466";
        when(this.csrfToken.getToken()).thenReturn(csrfTokenValue);

        // Submit from the UI spaceReference=X&name=Y&tocreate=terminal
        when(mockRequest.getParameter("spaceReference")).thenReturn("X");
        when(mockRequest.getParameter("name")).thenReturn("Y");
        when(mockRequest.getParameter("tocreate")).thenReturn("terminal");

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating X.Y instead of X.Y.WebHome because the tocreate parameter says "terminal".
        verify(mockURLFactory).createURL("X", "Y", "edit",
            "template=&parent=Main.WebHome&title=Y&form_token=" + csrfTokenValue, null, "xwiki",
            context);
    }