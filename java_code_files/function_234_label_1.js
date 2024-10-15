    void newDocumentFromURL() throws Exception
    {
        // new document = xwiki:X.Y
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("X"), "Y");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(true);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);
        String csrfTokenValue = "token42";
        when(this.csrfToken.getToken()).thenReturn(csrfTokenValue);

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        verify(mockURLFactory).createURL("X", "Y", "edit",
            "template=&parent=Main.WebHome&title=Y&form_token=" + csrfTokenValue,
            null, "xwiki",
            context);
    }