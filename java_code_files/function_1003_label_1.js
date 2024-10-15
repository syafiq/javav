    public void setDisabledGuestOrSuperadminUser() throws XWikiException
    {
        // With guest user we never save anything
        XWikiUser user = new XWikiUser((DocumentReference) null);
        user.setDisabled(true, mockitoOldcore.getXWikiContext());
        verify(userDocument, never())
            .setIntValue(same(userClassReference), any(String.class), any(Integer.class));
        verify(mockitoOldcore.getSpyXWiki(), never())
            .saveDocument(any(XWikiDocument.class), any(String.class), same(mockitoOldcore.getXWikiContext()));

        user.setDisabled(false, mockitoOldcore.getXWikiContext());
        verify(userDocument, never())
            .setIntValue(same(userClassReference), any(String.class), any(Integer.class));
        verify(mockitoOldcore.getSpyXWiki(), never())
            .saveDocument(any(XWikiDocument.class), any(String.class), same(mockitoOldcore.getXWikiContext()));

        // With superadmin user we never save anything
        user = new XWikiUser(XWikiRightService.SUPERADMIN_USER_FULLNAME);
        user.setDisabled(true, mockitoOldcore.getXWikiContext());
        verify(userDocument, never())
            .setIntValue(same(userClassReference), any(String.class), any(Integer.class));
        verify(mockitoOldcore.getSpyXWiki(), never())
            .saveDocument(any(XWikiDocument.class), any(String.class), same(mockitoOldcore.getXWikiContext()));

        user.setDisabled(false, mockitoOldcore.getXWikiContext());
        verify(userDocument, never())
            .setIntValue(same(userClassReference), any(String.class), any(Integer.class));
        verify(mockitoOldcore.getSpyXWiki(), never())
            .saveDocument(any(XWikiDocument.class), any(String.class), same(mockitoOldcore.getXWikiContext()));
    }