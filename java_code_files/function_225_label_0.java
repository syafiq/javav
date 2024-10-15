    void newDocumentWebHomeButTerminalFromURL() throws Exception
    {
        // new document = xwiki:X.Y.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("X", "Y"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(true);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);

        // Pass the tocreate=terminal request parameter
        when(mockRequest.getParameter("tocreate")).thenReturn("terminal");

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating X.Y instead of X.Y.WebHome because the tocreate parameter says "terminal".
        verify(mockURLFactory).createURL("X", "Y", "edit",
            "template=&parent=Main.WebHome&title=Y&form_token=" + CSRF_TOKEN_VALUE, null, "xwiki",
            context);
    }