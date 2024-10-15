    void existingDocumentNonTerminalFromUIDeprecated() throws Exception
    {
        // current document = xwiki:Main.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("Main"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(false);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);
        String csrfTokenValue = "token42778900";
        when(this.csrfToken.getToken()).thenReturn(csrfTokenValue);

        // Submit from the UI space=X&tocreate=space
        when(mockRequest.getParameter("space")).thenReturn("X");
        when(mockRequest.getParameter("tocreate")).thenReturn("space");

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating X.WebHome because the tocreate parameter says "space".
        verify(mockURLFactory).createURL("X", "WebHome", "edit",
            "template=&parent=Main.WebHome&title=X&form_token=" + csrfTokenValue, null, "xwiki",
            context);
    }