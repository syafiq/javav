    private OfficeDocumentView getView(ResourceReference reference, AttachmentReference attachmentReference,
        Map<String, ?> parameters) throws Exception
    {
        // Search the cache.
        String cacheKey =
            getCacheKey(attachmentReference.getDocumentReference(), attachmentReference.getName(), parameters);
        AttachmentOfficeDocumentView view = this.attachmentCache.get(cacheKey);
        Optional<XWikiAttachment> uploadedAttachment =
            this.temporaryAttachmentSessionsManager.getUploadedAttachment(attachmentReference);

        // It's possible that the attachment has been deleted. We need to catch such events and cleanup the cache.
        DocumentReference documentReference = attachmentReference.getDocumentReference();
        if (!this.documentAccessBridge.getAttachmentReferences(documentReference).contains(attachmentReference)
            && !uploadedAttachment.isPresent())
        {
            // If a cached view exists, flush it.
            if (view != null) {
                this.attachmentCache.remove(cacheKey);
            }
            throw new Exception(String.format("Attachment [%s] does not exist.", attachmentReference));
        }

        // Check if the view has expired.
        String currentVersion = this.documentAccessBridge.getAttachmentVersion(attachmentReference);
        if (view != null && !StringUtils.equals(currentVersion, view.getVersion())) {
            // Flush the cached view.
            this.attachmentCache.remove(cacheKey);
            view = null;
        }

        // If a view in not available, build one and cache it.
        if (view == null) {
            try (XDOMOfficeDocument xdomOfficeDocument = createXDOM(attachmentReference, parameters)) {
                String attachmentVersion = this.documentAccessBridge.getAttachmentVersion(attachmentReference);
                if (attachmentVersion == null && uploadedAttachment.isPresent()) {
                    attachmentVersion = "temp";
                }
                XDOM xdom = xdomOfficeDocument.getContentDocument();
                // We use only the file name from the resource reference because the rest of the information is
                // specified by the owner document reference. This way we ensure the path to the temporary files
                // doesn't contain redundant information and so it remains as small as possible (considering that the
                // path length is limited on some environments).
                Set<File> temporaryFiles = processImages(xdom, xdomOfficeDocument.getArtifactsFiles(),
                    attachmentReference.getDocumentReference(), parameters);
                view = new AttachmentOfficeDocumentView(reference, attachmentReference, attachmentVersion, xdom,
                    temporaryFiles);

                this.attachmentCache.set(cacheKey, view);
            }
        }

        // We have to clone the cached XDOM to protect it from the rendering transformations. For instance, macro
        // transformations must be executed even when the XDOM is taken from the cache.
        return view;
    }