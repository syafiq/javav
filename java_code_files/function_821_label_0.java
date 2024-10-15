    public boolean action(XWikiContext context) throws XWikiException
    {
        // CSRF prevention
        if (!csrfTokenCheck(context)) {
            return false;
        }

        RollbackForm form = (RollbackForm) context.getForm();
        if (!"1".equals(form.getConfirm())) {
            return true;
        }

        XWiki xwiki = context.getWiki();
        XWikiResponse response = context.getResponse();
        XWikiDocument doc = context.getDoc();

        String rev = form.getRev();
        String language = form.getLanguage();

        // We don't clone the document here because the rollback method does it before making modifications.
        XWikiDocument tdoc = getTranslatedDocument(doc, language, context);

        // Support for the "previous" pseudoversions.
        if ("previous".equals(rev)) {
            XWikiDocumentArchive archive = tdoc.loadDocumentArchive();

            // Note: Using Object to try to avoid to use jrcs objects.
            Object previousVersion = archive.getPrevVersion(archive.getLatestVersion());
            if (previousVersion != null) {
                rev = previousVersion.toString();
            } else {
                // Some inexistent version, since we have found no previous version in the archive.
                rev = "-1";
            }
        }

        // Perform the rollback.
        xwiki.rollback(tdoc, rev, true, true, context);

        // Forward to view.
        String redirect = Utils.getRedirect("view", context);
        sendRedirect(response, redirect);
        return false;
    }