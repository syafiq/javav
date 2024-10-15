    public void setDisabled(boolean disable, XWikiContext context)
    {
        // We don't modify any information for guest and superadmin.
        if (!isGuest() && !isSuperAdmin()) {
            int activeFlag = (disable) ? 0 : 1;
            try {
                XWikiDocument userdoc = getUserDocument(context);
                userdoc.setIntValue(getUserClassReference(userdoc.getDocumentReference().getWikiReference()),
                    ACTIVE_PROPERTY, activeFlag);
                userdoc.setAuthorReference(context.getUserReference());
                context.getWiki().saveDocument(userdoc,
                    localizePlainOrKey("core.users." + (disable ? "disable" : "enable") + ".saveComment"), context);
            } catch (XWikiException e) {
                this.logger.error("Error while setting active status of user [{}]", getUser(), e);
            }
        }
    }