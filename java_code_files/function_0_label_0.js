    public void beforeEach()
    {
        when(this.documentReferenceUserReferenceResolver.resolve(USER_DOCUMENT_REFERENCE)).thenReturn(USER_REFERENCE);
        when(this.documentReferenceUserReferenceSerializer.serialize(USER_REFERENCE)).thenReturn(USER_DOCUMENT_REFERENCE);

        this.oldcore.getXWikiContext().setUserReference(USER_DOCUMENT_REFERENCE);
    }