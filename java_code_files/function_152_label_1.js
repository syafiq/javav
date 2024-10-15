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
        for (File artifact : xdomOfficeDocument.getArtifactsFiles()) {

            AttachmentReference artifactReference = new AttachmentReference(artifact.getName(), targetDocRef);
            try (FileInputStream fis = new FileInputStream(artifact)) {
                documentAccessBridge.setAttachmentContent(artifactReference, IOUtils.toByteArray(fis));
            }
        }
        String result = xdomOfficeDocument.getContentAsString("annotatedxhtml/1.0");
        xdomOfficeDocument.close();
        return result;
    }