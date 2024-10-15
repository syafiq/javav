    private void checkRegistrationAuthorization(XWikiDocument document, Scope scope) throws AccessDeniedException
    {
        EntityReference entityReference;
        switch (scope) {
            case GLOBAL:
                this.authorizationManager.checkAccess(Right.PROGRAM, document.getAuthorReference(), null);
                this.authorizationManager.checkAccess(Right.PROGRAM, document.getContentAuthorReference(), null);
                break;
            case WIKI:
                entityReference = document.getDocumentReference().getWikiReference();
                this.authorizationManager.checkAccess(Right.ADMIN, document.getAuthorReference(), entityReference);
                this.authorizationManager.checkAccess(Right.ADMIN, document.getContentAuthorReference(),
                    entityReference);
                break;
            case USER:
                if (this.configuration.isRestrictUserTranslations()) {
                    entityReference = document.getDocumentReference();
                    this.authorizationManager.checkAccess(Right.SCRIPT, document.getAuthorReference(), entityReference);
                    this.authorizationManager.checkAccess(Right.SCRIPT, document.getContentAuthorReference(),
                        entityReference);
                }
                break;
            default:
                break;
        }
    }