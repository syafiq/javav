    void existingDocumentNonTerminalFromUIDeprecatedCheckEscaping() throws Exception
    {
        // current document = xwiki:Main.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("Main"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(false);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);
        String csrfTokenValue = "token425553";
        when(this.csrfToken.getToken()).thenReturn(csrfTokenValue);

        // Submit from the UI space=X.Y&tocreate=space
        when(mockRequest.getParameter("space")).thenReturn("X.Y");
        when(mockRequest.getParameter("tocreate")).thenReturn("space");

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note1: The space parameter was previously considered as space name, not space reference, so it is escaped.
        // Note2: We are creating X\.Y.WebHome because the tocreate parameter says "space".
        verify(mockURLFactory).createURL("X\\.Y", "WebHome", "edit",
            "template=&parent=Main.WebHome&title=X.Y&form_token=" + csrfTokenValue, null,
            "xwiki", context);
    }