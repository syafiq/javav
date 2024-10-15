    void newDocumentButNonTerminalFromURL() throws Exception
    {
        // new document = xwiki:X.Y
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("X"), "Y");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(true);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);

        // Pass the tocreate=nonterminal request parameter
        when(mockRequest.getParameter("tocreate")).thenReturn("nonterminal");

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Verify that the token has been validated.
        verify(this.csrfToken).isTokenValid(CSRF_TOKEN_VALUE);

        verify(mockURLFactory).createURL("X.Y", "WebHome", "edit",
            "template=&parent=Main.WebHome&title=Y&form_token=" + CSRF_TOKEN_VALUE, null,
            "xwiki", context);
    }