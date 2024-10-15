    public boolean isEditWithChangeRequestAllowed(DocumentReference documentReference) throws ChangeRequestException
    {
        UserReference currentUserReference = this.currentUserReferenceResolver.resolve(CurrentUserReference.INSTANCE);
        return this.changeRequestRightsManager.isEditWithChangeRequestAllowed(currentUserReference, documentReference);
    }