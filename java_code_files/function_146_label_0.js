    private void setAttachmentContent(XWikiDocument doc, String attachmentFilename, InputStream attachmentData,
        XWikiContext xcontext) throws Exception
    {
        if (doc.getAttachment(attachmentFilename) == null) {
            doc.setComment("Add new attachment " + attachmentFilename);
        } else {
            doc.setComment("Update attachment " + attachmentFilename);
        }

        doc.setAttachment(attachmentFilename, attachmentData, xcontext);

        doc.setAuthorReference(getCurrentUserReference());
        if (doc.isNew()) {
            doc.setCreatorReference(getCurrentUserReference());
        }

        xcontext.getWiki().saveDocument(doc, xcontext);
    }