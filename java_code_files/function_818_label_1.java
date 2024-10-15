    public void deleteDocumentVersions(XWikiDocument document, String version1, String version2, XWikiContext context)
        throws XWikiException
    {
        Version v1 = new Version(version1);
        Version v2 = new Version(version2);

        // Find the lower and upper bounds
        Version upperBound = v1;
        Version lowerBound = v2;
        if (upperBound.compareVersions(lowerBound) < 0) {
            Version tmp = upperBound;
            upperBound = lowerBound;
            lowerBound = tmp;
        }

        XWikiDocumentArchive archive = document.getDocumentArchive(context);

        if (archive.getNodes(upperBound, lowerBound).isEmpty()) {
            throw new XWikiException(XWikiException.MODULE_XWIKI,
                XWikiException.ERROR_XWIKI_STORE_HIBERNATE_UNEXISTANT_VERSION,
                String.format("Cannot find any revision to delete matching the range defined by [%s] and [%s]",
                    lowerBound, upperBound));
        }
        // Remove the versions
        archive.removeVersions(upperBound, lowerBound, context);

        // Is this the last remaining version? If so, then recycle the document.
        if (archive.getLatestVersion() == null) {
            // Wrap the work as a batch operation.
            BatchOperationExecutor batchOperationExecutor = Utils.getComponent(BatchOperationExecutor.class);
            batchOperationExecutor.execute(() -> {
                if (document.getLocale().equals(Locale.ROOT)) {
                    context.getWiki().deleteAllDocuments(document, context);
                } else {
                    // Only delete the translation
                    context.getWiki().deleteDocument(document, context);
                }
            });
        } else {
            // Notify before versions delete
            getObservationManager()
                .notify(new DocumentVersionRangeDeletingEvent(document.getDocumentReferenceWithLocale(),
                    lowerBound.toString(), upperBound.toString()), document, context);

            // Update the archive
            context.getWiki().getVersioningStore().saveXWikiDocArchive(archive, true, context);
            // Make sure the cached document archive is updated too
            XWikiDocument cachedDocument =
                context.getWiki().getDocument(document.getDocumentReferenceWithLocale(), context);
            cachedDocument.setDocumentArchive(archive);

            // There are still some versions left.
            // If we delete the most recent (current) version, then rollback to latest undeleted version.
            Version previousVersion = archive.getLatestVersion();
            if (!document.getRCSVersion().equals(previousVersion)) {
                context.getWiki().rollback(document, previousVersion.toString(), false, context);
            }

            // Notify after versions delete
            getObservationManager()
                .notify(new DocumentVersionRangeDeletedEvent(document.getDocumentReferenceWithLocale(),
                    lowerBound.toString(), upperBound.toString()), document, context);
        }
    }