    void setAttachmentContent(boolean useStream, MockitoOldcore oldcore) throws Exception
    {
        DocumentReference documentReference = new DocumentReference("Wiki", "Space", "Page");
        String fileName = "image.png";
        AttachmentReference attachmentReference = new AttachmentReference(fileName, documentReference);
        byte[] attachmentContent = new byte[] { 42, 23 };
        if (useStream) {
            this.documentAccessBridge.setAttachmentContent(attachmentReference,
                new ByteArrayInputStream(attachmentContent));
        } else {
            this.documentAccessBridge.setAttachmentContent(attachmentReference, attachmentContent);
        }
        XWikiDocument document = oldcore.getSpyXWiki().getDocument(documentReference, oldcore.getXWikiContext());
        XWikiAttachment attachment = document.getAttachment(fileName);
        assertNotNull(attachment);
        assertEquals(fileName, attachment.getFilename());
        assertArrayEquals(attachmentContent,
            IOUtils.toByteArray(attachment.getAttachmentContent(oldcore.getXWikiContext()).getContentInputStream()));
    }