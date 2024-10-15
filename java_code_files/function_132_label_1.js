    void viewExistingOfficeAttachmentWithCacheMiss(MockitoComponentManager componentManager) throws Exception
    {
        when(attachmentCache.get(CACHE_KEY)).thenReturn(null);
        when(documentAccessBridge.getAttachmentReferences(ATTACHMENT_REFERENCE.getDocumentReference())).thenReturn(
            Arrays.asList(ATTACHMENT_REFERENCE));
        when(documentAccessBridge.getAttachmentVersion(ATTACHMENT_REFERENCE)).thenReturn(ATTACHMENT_VERSION);

        ByteArrayInputStream attachmentContent = new ByteArrayInputStream(new byte[256]);
        when(documentAccessBridge.getAttachmentContent(ATTACHMENT_REFERENCE)).thenReturn(attachmentContent);

        XDOMOfficeDocument xdomOfficeDocument =
            new XDOMOfficeDocument(new XDOM(new ArrayList<Block>()), Collections.emptySet(), componentManager, null);
        when(
            officeDocumentBuilder.build(attachmentContent, ATTACHMENT_REFERENCE.getName(),
                ATTACHMENT_REFERENCE.getDocumentReference(), false)).thenReturn(xdomOfficeDocument);

        this.officeResourceViewer.createView(ATTACHMENT_RESOURCE_REFERENCE, DEFAULT_VIEW_PARAMETERS);

        verify(attachmentCache).set(eq(CACHE_KEY), any(AttachmentOfficeDocumentView.class));
    }