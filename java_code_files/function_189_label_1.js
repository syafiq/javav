    private void filterImageSource(Attr source, DocumentReference targetDocumentReference)
    {
        String fileName = null;
        try {
            fileName = getFileName(source);
        } catch (Exception e) {
            this.logger.warn("Failed to extract the image file name. Root cause is [{}]",
                ExceptionUtils.getRootCauseMessage(e));
            this.logger.debug("Full stacktrace is: ", e);
        }
        if (StringUtils.isEmpty(fileName)) {
            return;
        }

        // Set image source attribute relative to the reference document.
        AttachmentReference attachmentReference = new AttachmentReference(fileName, targetDocumentReference);
        source.setValue(this.documentAccessBridge.getAttachmentURL(attachmentReference, false));

        ResourceReference imageReference = new ResourceReference(fileName, ResourceType.ATTACHMENT);
        imageReference.setTyped(false);
        Comment beforeComment = source.getOwnerDocument().createComment(
            XMLUtils.escapeXMLComment("startimage:" + this.xhtmlMarkerSerializer.serialize(imageReference)));
        Comment afterComment = source.getOwnerDocument().createComment("stopimage");
        Element image = source.getOwnerElement();
        image.getParentNode().insertBefore(beforeComment, image);
        image.getParentNode().insertBefore(afterComment, image.getNextSibling());
    }