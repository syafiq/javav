    public void setAttachmentContent(AttachmentReference attachmentReference, InputStream attachmentData)
        throws Exception
    {
        XWikiContext xcontext = getContext();
        XWikiDocument doc = xcontext.getWiki().getDocument(attachmentReference.getDocumentReference(), xcontext);

        setAttachmentContent(doc, attachmentReference.getName(), attachmentData, xcontext);
    }