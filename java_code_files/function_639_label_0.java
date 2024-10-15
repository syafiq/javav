    public boolean matches(Object otherEvent)
    {
        if (otherEvent == this) {
            return true;
        }

        boolean isMatching = false;

        if (this.getClass().isAssignableFrom(otherEvent.getClass())) {
            GeneralMailConfigurationUpdatedEvent other = (GeneralMailConfigurationUpdatedEvent) otherEvent;
            isMatching = this.wikiId == null || other.wikiId == null || Objects.equals(this.wikiId, other.wikiId);
        }

        return isMatching;
    }