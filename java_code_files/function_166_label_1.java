    private void attachArtifacts(Set<File> artifactFiles, DocumentReference targetDocumentReference)
    {
        for (File artifact : artifactFiles) {
            AttachmentReference attachmentReference =
                new AttachmentReference(artifact.getName(), targetDocumentReference);
            try (FileInputStream fis = new FileInputStream(artifact)) {
                this.docBridge.setAttachmentContent(attachmentReference, IOUtils.toByteArray(fis));
            } catch (Exception ex) {
                // Log the error and skip the artifact.
                this.logger.error("Error while attaching artifact.", ex);
            }
        }
    }