    void newDocumentWebHomeFromURL() throws Exception
    {
        // new document = xwiki:X.Y.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("X", "Y"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(true);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);
        String csrfTokenValue = "token34342";
        when(this.csrfToken.getToken()).thenReturn(csrfTokenValue);

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note1: The bebavior is the same for both a top level space and a child space WebHome.
        // Note2: The title is not "WebHome", but "Y" (the space's name) to avoid exposing "WebHome" in the UI.
        verify(mockURLFactory).createURL("X.Y", "WebHome", "edit",
            "template=&parent=Main.WebHome&title=Y&form_token=" + csrfTokenValue, null,
            "xwiki", context);
    }