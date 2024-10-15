    private static ObjectReference getConfigurationObjectReference(String wikiId)
    {
        DocumentReference documentReference = new DocumentReference(
            AbstractGeneralMailConfigClassDocumentConfigurationSource.MAILCONFIG_REFERENCE, new WikiReference(wikiId));
        DocumentReference configClassReference = new DocumentReference(
            AbstractGeneralMailConfigClassDocumentConfigurationSource.GENERAL_MAILCONFIGCLASS_REFERENCE,
            new WikiReference(wikiId));
        return new BaseObjectReference(configClassReference, 0, documentReference);
    }