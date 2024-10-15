    void existingDocumentNonTerminalFromUIDeprecatedIgnoringPage() throws Exception
    {
        // current document = xwiki:Main.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("Main"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(false);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);

        // Submit from the UI space=X&page=Y&tocreate=space
        when(mockRequest.getParameter("space")).thenReturn("X");
        when(mockRequest.getParameter("page")).thenReturn("Y");
        when(mockRequest.getParameter("tocreate")).thenReturn("space");

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating X.WebHome instead of X.Y because the tocreate parameter says "space" and the page
        // parameter is ignored.
        verify(mockURLFactory).createURL("X", "WebHome", "edit",
            "template=&parent=Main.WebHome&title=X&form_token=" + CSRF_TOKEN_VALUE, null, "xwiki",
            context);
    }