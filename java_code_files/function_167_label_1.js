    public void save(XDOMOfficeDocument doc, DocumentReference documentReference, String syntaxId,
        DocumentReference parentReference, String title, boolean append) throws Exception
    {
        // First check if the user has edit rights on the target document.
        if (!this.contextualAuthorizationManager.hasAccess(Right.EDIT, documentReference)) {
            String message = "You do not have edit rights on [%s] document.";
            throw new OfficeImporterException(String.format(message, documentReference));
        }

        // Save.
        if (this.docBridge.exists(documentReference) && append) {
            // Check whether existing document's syntax is same as target syntax.
            String currentSyntaxId =
                this.docBridge.getTranslatedDocumentInstance(documentReference).getSyntax().toIdString();
            if (!currentSyntaxId.equals(syntaxId)) {
                String message =
                    "The target page [%s] exists but its syntax [%s] is different from the specified syntax [%s]";
                throw new OfficeImporterException(String.format(message, documentReference, currentSyntaxId, syntaxId));
            }

            // Append the content.
            String currentContent = this.docBridge.getDocumentContent(documentReference, null);
            String newContent = currentContent + "\n" + doc.getContentAsString(syntaxId);
            this.docBridge.setDocumentContent(documentReference, newContent, "Updated by office importer.", false);
        } else {
            this.docBridge.setDocumentSyntaxId(documentReference, syntaxId);
            this.docBridge.setDocumentContent(documentReference, doc.getContentAsString(syntaxId),
                "Created by office importer.", false);

            // Set parent if provided.
            if (null != parentReference) {
                this.docBridge.setDocumentParentReference(documentReference, parentReference);
            }

            // If no title is specified, try to extract one.
            String docTitle = (null == title) ? doc.getTitle() : title;

            // Set title if applicable.
            if (null != docTitle) {
                this.docBridge.setDocumentTitle(documentReference, docTitle);
            }
        }

        // Finally attach all the artifacts into target document.
        attachArtifacts(doc.getArtifactsFiles(), documentReference);
    }