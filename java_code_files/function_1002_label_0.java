    public void setDisabledGuestOrSuperadminUser() throws XWikiException
    {
        // With guest user we never save anything
        XWikiUser user = new XWikiUser((DocumentReference) null);
        user.setDisabled(true, this.mockitoOldcore.getXWikiContext());
        verify(this.userDocument, never())
            .setIntValue(same(this.userClassReference), any(String.class), any(Integer.class));
        verify(this.mockitoOldcore.getSpyXWiki(), never())
            .saveDocument(any(XWikiDocument.class), any(String.class), same(this.mockitoOldcore.getXWikiContext()));

        user.setDisabled(false, this.mockitoOldcore.getXWikiContext());
        verify(this.userDocument, never())
            .setIntValue(same(this.userClassReference), any(String.class), any(Integer.class));
        verify(this.mockitoOldcore.getSpyXWiki(), never())
            .saveDocument(any(XWikiDocument.class), any(String.class), same(this.mockitoOldcore.getXWikiContext()));

        // With superadmin user we never save anything
        user = new XWikiUser(XWikiRightService.SUPERADMIN_USER_FULLNAME);
        user.setDisabled(true, this.mockitoOldcore.getXWikiContext());
        verify(this.userDocument, never())
            .setIntValue(same(this.userClassReference), any(String.class), any(Integer.class));
        verify(this.mockitoOldcore.getSpyXWiki(), never())
            .saveDocument(any(XWikiDocument.class), any(String.class), same(this.mockitoOldcore.getXWikiContext()));

        user.setDisabled(false, this.mockitoOldcore.getXWikiContext());
        verify(this.userDocument, never())
            .setIntValue(same(this.userClassReference), any(String.class), any(Integer.class));
        verify(this.mockitoOldcore.getSpyXWiki(), never())
            .saveDocument(any(XWikiDocument.class), any(String.class), same(this.mockitoOldcore.getXWikiContext()));
    }