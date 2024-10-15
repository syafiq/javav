    void saveWithOverwrite() throws Exception
    {
        XDOMOfficeDocument doc = mock(XDOMOfficeDocument.class);
        DocumentReference documentReference = new DocumentReference("wiki", "Space", "Page");
        DocumentReference parentReference = new DocumentReference("wiki", "Space", "Parent");
        String syntaxId = "test/1.0";
        String title = "Office Document Title";
        String content = "Office Document Content";
        String fileName = "logo.png";
        byte[] fileContent = new byte[] {65, 82};
        File artifact = new File(tempDir, fileName);
        try (FileOutputStream fos = new FileOutputStream(artifact)) {
            IOUtils.write(fileContent, fos);
        }

        when(this.contextualAuthorizationManager.hasAccess(Right.EDIT, documentReference)).thenReturn(true);
        when(doc.getContentAsString(syntaxId)).thenReturn(content);
        when(doc.getArtifactsMap())
            .thenReturn(Collections.singletonMap(fileName, new FileOfficeDocumentArtifact(fileName, artifact)));
        // Store all attachment contents that are set on the mock.
        Map<AttachmentReference, byte[]> attachmentContents = new HashMap<>();
        doAnswer((invocation) -> attachmentContents.put(
            invocation.getArgument(0, AttachmentReference.class),
            IOUtils.toByteArray(invocation.getArgument(1, InputStream.class))
        )).when(this.documentAccessBridge)
            .setAttachmentContent(any(AttachmentReference.class), any(InputStream.class));

        this.modelBridge.save(doc, documentReference, syntaxId, parentReference, title, false);

        verify(documentAccessBridge).setDocumentSyntaxId(documentReference, syntaxId);
        verify(documentAccessBridge).setDocumentContent(documentReference, content, "Created by office importer.",
            false);
        verify(documentAccessBridge).setDocumentParentReference(documentReference, parentReference);
        verify(documentAccessBridge).setDocumentTitle(documentReference, title);
        assertEquals(1, attachmentContents.size());
        AttachmentReference expectedAttachmentReference = new AttachmentReference(fileName, documentReference);
        assertEquals(expectedAttachmentReference, attachmentContents.keySet().iterator().next());
        assertArrayEquals(fileContent, attachmentContents.get(expectedAttachmentReference));
    }