    public List<Event> getEvents()
    {
        String serializedClassReference = this.referenceSerializer.serialize(
            AbstractGeneralMailConfigClassDocumentConfigurationSource.GENERAL_MAILCONFIGCLASS_REFERENCE);
        RegexEntityReference filter = BaseObjectReference.any(serializedClassReference);

        return List.of(new XObjectAddedEvent(filter), new XObjectDeletedEvent(filter), new XObjectUpdatedEvent(filter));
    }