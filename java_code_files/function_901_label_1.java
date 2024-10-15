    public ComponentDocumentTranslationBundle(String idPrefix, DocumentReference documentReference,
        ComponentManager componentManager, TranslationMessageParser translationMessageParser,
        ComponentDescriptor<TranslationBundle> descriptor) throws ComponentLookupException
    {
        super(idPrefix, documentReference, componentManager, translationMessageParser);

        this.descriptor = descriptor;
    }