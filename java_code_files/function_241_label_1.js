    void existingDocumentFromUIDeprecated() throws Exception
    {
        // current document = xwiki:Main.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("Main"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(false);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);
        String csrfTokenValue = "token4233311";
        when(this.csrfToken.getToken()).thenReturn(csrfTokenValue);

        // Submit from the UI space=X&page=Y
        when(mockRequest.getParameter("space")).thenReturn("X");
        when(mockRequest.getParameter("page")).thenReturn("Y");

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating X.Y since the deprecated parameters were creating terminal documents by default.
        verify(mockURLFactory).createURL("X", "Y", "edit",
            "template=&parent=Main.WebHome&title=Y&form_token=" + csrfTokenValue, null, "xwiki",
            context);
    }