    private void setAttachmentContent(XWikiDocument doc, String attachmentFilename, byte[] attachmentData,
        XWikiContext xcontext) throws Exception
    {
        setAttachmentContent(doc, attachmentFilename,
            new ByteArrayInputStream(attachmentData != null ? attachmentData : new byte[0]), xcontext);
    }