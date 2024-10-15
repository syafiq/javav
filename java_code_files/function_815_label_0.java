    public XWikiDocument rollback(final XWikiDocument tdoc, String rev, boolean addRevision,
        boolean triggeredByUser, XWikiContext xcontext) throws XWikiException
    {
        LOGGER.debug("Rolling back [{}] to version [{}]", tdoc, rev);

        // Clone the document before modifying to avoid concurrency issues
        XWikiDocument document = tdoc.clone();

        XWikiDocument rolledbackDoc = getDocumentRevisionProvider().getRevision(tdoc, rev);

        // Restore attachments
        if ("1".equals(getConfiguration().getProperty("xwiki.store.rollbackattachmentwithdocuments", "1"))) {
            LOGGER.debug("Checking attachments");

            for (XWikiAttachment rolledbackAttachment : rolledbackDoc.getAttachmentList()) {
                String filename = rolledbackAttachment.getFilename();
                XWikiAttachment attachment = document.getAttachment(filename);

                if (attachment == null) {
                    // The attachment has been deleted, search and restore it
                    LOGGER.debug("Deleted attachment: [{}]", filename);

                    // Restore content and archive from the recycle bin
                    restoreDeletedAttachment(rolledbackAttachment, xcontext);
                } else {
                    XWikiAttachment attachmentRevision =
                        attachment.getAttachmentRevision(rolledbackAttachment.getVersion(), xcontext);

                    // We compare the number of milliseconds instead of the date objects directly because Hibernate can
                    // return java.sql.Timestamp for date fields and the JavaDoc says that Timestamp.equals(Object)
                    // doesn't return true if the passed value is a java.util.Date object with the same number of
                    // milliseconds because the nanoseconds component of the passed date is unknown.
                    if (attachmentRevision == null
                        || attachmentRevision.getDate().getTime() != rolledbackAttachment.getDate().getTime()) {
                        // Recreated attachment
                        LOGGER.debug("Recreated attachment: [{}]", filename);

                        // Mark current attachment for deletion to not loose it
                        document.removeAttachment(attachment);

                        // Search and restore previously deleted one
                        // If the attachment trash is not available, don't lose the existing attachment
                        if (getAttachmentRecycleBinStore() != null) {
                            // Restore in the right version
                            restoreDeletedAttachment(rolledbackAttachment, xcontext);
                        }
                    } else {
                        // Restore content and archive from the recycle bin
                        rolledbackAttachment.apply(attachmentRevision);
                    }
                }
            }
        }

        document.apply(rolledbackDoc);

        // Prepare the XWikiDocument before save.
        document.setAuthorReference(xcontext.getUserReference());
        document.setContentAuthorReference(xcontext.getUserReference());

        // Note: In the case where we don't add a new revision, there'll be no new entry in the history and thus
        // the author displayed for the current document must be the same as the last revision in the history. Set
        // the original metadata author to reflect this.
        if (!addRevision) {
            document.getAuthors().setOriginalMetadataAuthor(rolledbackDoc.getAuthors().getOriginalMetadataAuthor());
        }

        // Make sure the history is not modified if addRevision is disabled
        String message;
        if (!addRevision) {
            document.setVersion(rev);
            document.setMetaDataDirty(false);
            document.setContentDirty(false);
            message = document.getComment();
        } else {
            // Make sure to save a new version even if nothing changed
            document.setMetaDataDirty(true);
            message = localizePlainOrKey("core.comment.rollback", rev);
        }

        if (triggeredByUser) {
            checkSavingDocument(xcontext.getUserReference(), document, message, false, xcontext);
        }

        ObservationManager om = getObservationManager();
        if (om != null) {
            // Notify listeners about the document that is going to be rolled back.
            // Note that for the moment the event being send is a bridge event, as we are still passing around
            // an XWikiDocument as source and an XWikiContext as data.
            om.notify(new DocumentRollingBackEvent(document.getDocumentReference(), rev), document, xcontext);
        }

        XWikiDocument originalDocument = document.getOriginalDocument();

        saveDocument(document, message, xcontext);

        // Since XWiki#saveDocument resets the original document, we need to temporarily put it back to send
        // notifications.
        XWikiDocument newOriginalDocument = document.getOriginalDocument();
        document.setOriginalDocument(originalDocument);

        try {
            if (om != null) {
                // Notify listeners about the document that was rolled back.
                // Note that for the moment the event being send is a bridge event, as we are still passing around an
                // XWikiDocument as source and an XWikiContext as data.
                om.notify(new DocumentRolledBackEvent(document.getDocumentReference(), rev), document, xcontext);
            }
        } finally {
            document.setOriginalDocument(newOriginalDocument);
        }

        return document;
    }