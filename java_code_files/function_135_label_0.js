    default void setAttachmentContent(AttachmentReference attachmentReference, InputStream attachmentData)
        throws Exception
    {
        setAttachmentContent(attachmentReference, IOUtils.toByteArray(attachmentData));
    }