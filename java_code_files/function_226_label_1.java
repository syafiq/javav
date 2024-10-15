    void newDocumentWebHomeTopLevelFromURL() throws Exception
    {
        // new document = xwiki:X.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("X"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(true);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);
        String csrfTokenValue = "token4234";
        when(this.csrfToken.getToken()).thenReturn(csrfTokenValue);

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: The title is not "WebHome", but "X" (the space's name) to avoid exposing "WebHome" in the UI.
        verify(mockURLFactory).createURL("X", "WebHome", "edit",
            "template=&parent=Main.WebHome&title=X&form_token=" + csrfTokenValue, null, "xwiki",
            context);
    }