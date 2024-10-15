    void toHTMLTemporaryUploadedAttachment() throws Exception
    {
        when(this.authorization.hasAccess(Right.EDIT, ATTACHMENT_REFERENCE)).thenReturn(true);
        when(this.documentAccessBridge.getAttachmentVersion(ATTACHMENT_REFERENCE)).thenReturn(null);
        XWikiAttachment attachment = mock(XWikiAttachment.class);
        when(this.temporaryAttachmentSessionsManager.getUploadedAttachment(ATTACHMENT_REFERENCE))
            .thenReturn(Optional.of(attachment));

        when(this.officeServer.getState()).thenReturn(ServerState.CONNECTED);

        InputStream attachmentContent = mock(InputStream.class);
        when(attachment.getContentInputStream(this.context)).thenReturn(attachmentContent);

        XDOMOfficeDocument xdomOfficeDocument = mock(XDOMOfficeDocument.class);
        when(documentBuilder.build(attachmentContent, "my.doc", ATTACHMENT_REFERENCE.getDocumentReference(), true))
            .thenReturn(xdomOfficeDocument);
        when(xdomOfficeDocument.getArtifactsFiles()).thenReturn(Collections.emptySet());
        when(xdomOfficeDocument.getContentAsString("annotatedxhtml/1.0")).thenReturn("test");

        Map<String, Object> parameters = Collections.singletonMap("filterStyles", "true");
        assertEquals("test", officeAttachmentImporter.toHTML(ATTACHMENT_REFERENCE, parameters));
    }