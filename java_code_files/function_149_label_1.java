    void viewTemporaryUploadedOfficeAttachmentWithCacheHit(MockitoComponentManager componentManager) throws Exception
    {
        AttachmentOfficeDocumentView officeDocumentView =
            new AttachmentOfficeDocumentView(ATTACHMENT_RESOURCE_REFERENCE, ATTACHMENT_REFERENCE, ATTACHMENT_VERSION,
                new XDOM(new ArrayList<Block>()), new HashSet<File>());
        when(attachmentCache.get(CACHE_KEY)).thenReturn(officeDocumentView);

        when(documentAccessBridge.getAttachmentReferences(ATTACHMENT_REFERENCE.getDocumentReference())).thenReturn(
            Collections.emptyList());
        when(documentAccessBridge.getAttachmentVersion(ATTACHMENT_REFERENCE)).thenReturn(null);
        XWikiAttachment attachment = mock(XWikiAttachment.class);
        when(temporaryAttachmentSessionsManager.getUploadedAttachment(ATTACHMENT_REFERENCE))
            .thenReturn(Optional.of(attachment));

        ByteArrayInputStream attachmentContent = new ByteArrayInputStream(new byte[256]);
        when(attachment.getContentInputStream(this.context)).thenReturn(attachmentContent);

        XDOMOfficeDocument xdomOfficeDocument =
            new XDOMOfficeDocument(new XDOM(new ArrayList<Block>()), Collections.emptySet(), componentManager, null);
        when(
            officeDocumentBuilder.build(attachmentContent, ATTACHMENT_REFERENCE.getName(),
                ATTACHMENT_REFERENCE.getDocumentReference(), false)).thenReturn(xdomOfficeDocument);

        this.officeResourceViewer.createView(ATTACHMENT_RESOURCE_REFERENCE, DEFAULT_VIEW_PARAMETERS);

        verify(attachmentCache).set(eq(CACHE_KEY), any(AttachmentOfficeDocumentView.class));
    }