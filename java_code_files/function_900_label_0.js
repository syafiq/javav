    protected XWikiDocument getDocumentLocaleBundle(Locale locale) throws Exception
    {
        XWikiDocument document = super.getDocumentLocaleBundle(locale);

        if (document != null && !document.isNew()) {
            XWikiContext context = this.contextProvider.get();
            XWiki xwiki = context.getWiki();
            XWikiDocument defaultLocaleDocument = xwiki.getDocument(this.documentReference, context);

            if (defaultLocaleDocument != document) {
                // We only need to check rights for non-default locales.
                try {
                    this.factory.checkRegistrationAuthorizationForDocumentLocaleBundle(document, defaultLocaleDocument);
                } catch (AccessDeniedException e) {
                    this.logger.warn("Failed to load and register the translation for locale [{}] from document [{}]. "
                        + "Falling back to default locale.", locale, document.getDocumentReference());
                    // We return the default translation bundle if the requested one has permission issues.
                    return defaultLocaleDocument;
                }
            }
        }

        return document;
    }