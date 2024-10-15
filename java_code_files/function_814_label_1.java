    public boolean action(XWikiContext context) throws XWikiException
    {
        DeleteVersionsForm form = (DeleteVersionsForm) context.getForm();
        if (!form.isConfirmed() || !csrfTokenCheck(context)) {
            return true;
        }

        XWikiDocument doc = context.getDoc();
        String language = form.getLanguage();
        XWikiDocument tdoc = doc.getTranslatedDocument(language, context);        
        XWikiDocumentArchive archive = tdoc.getDocumentArchive(context);

        // Get the versions
        Version[] versions = getVersionsFromForm(form, archive);
        Version v1 = versions[0];
        Version v2 = versions[1];

        if (v1 != null && v2 != null) {
            context.getWiki().deleteDocumentVersions(tdoc, v1.toString(), v2.toString(), context);
        }

        sendRedirect(context);
        return false;
    }