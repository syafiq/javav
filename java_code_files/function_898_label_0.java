    protected XWikiDocument getDocumentLocaleBundle(Locale locale) throws Exception
    {
        XWikiContext context = this.contextProvider.get();

        if (context == null) {
            // No context for some reason, let's try later.
            return null;
        }

        XWiki xwiki = context.getWiki();

        if (xwiki == null) {
            // No XWiki instance ready, let's try later.
            return null;
        }

        XWikiDocument document = xwiki.getDocument(this.documentReference, context);

        if (locale != null && !locale.equals(Locale.ROOT) && !locale.equals(document.getDefaultLocale())) {
            document = xwiki.getDocument(new DocumentReference(document.getDocumentReference(), locale), context);
        }

        return document;
    }