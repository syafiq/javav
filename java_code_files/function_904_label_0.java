    protected void checkRegistrationAuthorizationForDocumentLocaleBundle(XWikiDocument document,
        XWikiDocument defaultLocaleDocument) throws AccessDeniedException
    {
        Scope scope = getScope(defaultLocaleDocument);
        if (scope != null && scope != Scope.ON_DEMAND) {
            checkRegistrationAuthorization(document, scope);
        }
    }