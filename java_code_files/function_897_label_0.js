    protected LocalizedTranslationBundle loadDocumentLocaleBundle(Locale locale) throws Exception
    {
        XWikiDocument document = getDocumentLocaleBundle(locale);

        if (document == null) {
            // Either no context or XWiki instance not ready, let's try later.
            return null;
        }

        if (document.isNew()) {
            // No document found for this locale.
            return LocalizedTranslationBundle.EMPTY;
        }

        String content = document.getContent();

        Properties properties = new Properties();
        properties.load(new StringReader(content));

        // Convert to LocalBundle.
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