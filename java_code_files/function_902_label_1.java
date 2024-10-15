    private ComponentDocumentTranslationBundle createComponentDocumentBundle(XWikiDocument document,
        ComponentDescriptor<TranslationBundle> descriptor) throws TranslationBundleDoesNotExistsException
    {
        ComponentDocumentTranslationBundle documentBundle;
        try {
            documentBundle =
                new ComponentDocumentTranslationBundle(ID_PREFIX, document.getDocumentReference(),
                    this.componentManagerProvider.get(), this.translationParser, descriptor);
        } catch (ComponentLookupException e) {
            throw new TranslationBundleDoesNotExistsException("Failed to create document bundle", e);
        }

        return documentBundle;
    }