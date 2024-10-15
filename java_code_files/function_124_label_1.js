    void viewANewVersionOfAnExistingOfficeAttachment(MockitoComponentManager componentManager) throws Exception
    {
        AttachmentOfficeDocumentView officeDocumentView =
            new AttachmentOfficeDocumentView(ATTACHMENT_RESOURCE_REFERENCE, ATTACHMENT_REFERENCE, ATTACHMENT_VERSION,
                new XDOM(new ArrayList<Block>()), new HashSet<File>());
        when(attachmentCache.get(CACHE_KEY)).thenReturn(officeDocumentView);

        when(documentAccessBridge.getAttachmentReferences(ATTACHMENT_REFERENCE.getDocumentReference())).thenReturn(
            Arrays.asList(ATTACHMENT_REFERENCE));
        when(documentAccessBridge.getAttachmentVersion(ATTACHMENT_REFERENCE)).thenReturn("2.1");

        ByteArrayInputStream attachmentContent = new ByteArrayInputStream(new byte[256]);
        when(documentAccessBridge.getAttachmentContent(ATTACHMENT_REFERENCE)).thenReturn(attachmentContent);

        XDOMOfficeDocument xdomOfficeDocument =
            new XDOMOfficeDocument(new XDOM(new ArrayList<Block>()), Collections.emptySet(), componentManager, null);
        when(
            officeDocumentBuilder.build(attachmentContent, ATTACHMENT_REFERENCE.getName(),
                ATTACHMENT_REFERENCE.getDocumentReference(), false)).thenReturn(xdomOfficeDocument);

        assertNotNull(this.officeResourceViewer.createView(ATTACHMENT_RESOURCE_REFERENCE, DEFAULT_VIEW_PARAMETERS));

        verify(attachmentCache).remove(CACHE_KEY);
        verify(attachmentCache).set(eq(CACHE_KEY), notNull());
    }