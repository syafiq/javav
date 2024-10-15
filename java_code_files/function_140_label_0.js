    private String convertAttachmentContent(AttachmentReference attachmentReference, boolean filterStyles)
        throws Exception
    {
        InputStream officeFileStream;
        if (this.documentAccessBridge.getAttachmentVersion(attachmentReference) != null) {
            officeFileStream = documentAccessBridge.getAttachmentContent(attachmentReference);
        } else {
            Optional<XWikiAttachment> uploadedAttachment =
                this.temporaryAttachmentSessionsManager.getUploadedAttachment(attachmentReference);
            if (uploadedAttachment.isPresent()) {
                officeFileStream = uploadedAttachment.get().getContentInputStream(this.contextProvider.get());
            } else {
                throw new OfficeImporterException(
                    String.format("Cannot find temporary uplodaded attachment [%s]", attachmentReference));
            }
        }
        String officeFileName = attachmentReference.getName();
        DocumentReference targetDocRef = attachmentReference.getDocumentReference();
        XDOMOfficeDocument xdomOfficeDocument;
        if (isPresentation(attachmentReference.getName())) {
            xdomOfficeDocument = presentationBuilder.build(officeFileStream, officeFileName, targetDocRef);
        } else {
            xdomOfficeDocument = documentBuilder.build(officeFileStream, officeFileName, targetDocRef, filterStyles);
        }
        // Attach the images extracted from the imported office document to the target wiki document.
        for (Map.Entry<String, OfficeDocumentArtifact> entry : xdomOfficeDocument.getArtifactsMap().entrySet()) {
            String filename = entry.getKey();
            OfficeDocumentArtifact artifact = entry.getValue();
            AttachmentReference artifactReference = new AttachmentReference(filename, targetDocRef);
            try (InputStream is = artifact.getContentInputStream()) {
                this.documentAccessBridge.setAttachmentContent(artifactReference, is);
            }
        }
        String result = xdomOfficeDocument.getContentAsString("annotatedxhtml/1.0");
        xdomOfficeDocument.close();
        return result;
    }