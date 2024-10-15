    void existingDocumentFromUICheckEscaping() throws Exception
    {
        // current document = xwiki:Main.WebHome
        DocumentReference documentReference = new DocumentReference("xwiki", Arrays.asList("Main"), "WebHome");
        XWikiDocument document = mock(XWikiDocument.class);
        when(document.getDocumentReference()).thenReturn(documentReference);
        when(document.isNew()).thenReturn(false);
        when(document.getLocalReferenceMaxLength()).thenReturn(255);
        context.setDoc(document);

        // Submit from the UI spaceReference=X.Y&name=Z
        when(mockRequest.getParameter("spaceReference")).thenReturn("X.Y");
        when(mockRequest.getParameter("name")).thenReturn("Z");

        // Run the action
        String result = action.render(context);

        // The tests are below this line!

        // Verify null is returned (this means the response has been returned)
        assertNull(result);

        // Note: We are creating X.Y.Z.WebHome since we default to non-terminal documents.
        verify(mockURLFactory).createURL("X.Y.Z", "WebHome", "edit",
            "template=&parent=Main.WebHome&title=Z&form_token=" + CSRF_TOKEN_VALUE, null,
            "xwiki", context);
    }