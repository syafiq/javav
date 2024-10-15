    protected LocalizedTranslationBundle loadDocumentLocaleBundle(Locale locale) throws Exception
    {
        XWikiContext context = this.contextProvider.get();

        if (context == null) {
            // No context for some reason, lets try later
            return null;
        }

        XWiki xwiki = context.getWiki();

        if (xwiki == null) {
            // No XWiki instance ready, lets try later
            return null;
        }

        XWikiDocument document = xwiki.getDocument(this.documentReference, context);

        if (locale != null && !locale.equals(Locale.ROOT) && !locale.equals(document.getDefaultLocale())) {
            document = xwiki.getDocument(new DocumentReference(document.getDocumentReference(), locale), context);

            if (document.isNew()) {
                // No document found for this locale
                return LocalizedTranslationBundle.EMPTY;
            }
        }

        String content = document.getContent();

        Properties properties = new Properties();
        properties.load(new StringReader(content));

        // Convert to LocalBundle
        DefaultLocalizedTranslationBundle localeBundle = new DefaultLocalizedTranslationBundle(this, locale);

        TranslationMessageParser parser = getTranslationMessageParser();

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
                String key = (String) entry.getKey();
                String message = (String) entry.getValue();

                TranslationMessage translationMessage = parser.parse(message);

                localeBundle.addTranslation(new DefaultTranslation(this.bundleContext, localeBundle, key,
                    translationMessage));
            }
        }

        return localeBundle;
    }