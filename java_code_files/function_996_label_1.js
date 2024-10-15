    public void setEmailCheckedGuestOrSuperadminUser() throws XWikiException
    {
        // With guest user we never save anything
        XWikiUser user = new XWikiUser((DocumentReference) null);
        user.setEmailChecked(true, mockitoOldcore.getXWikiContext());
        verify(userDocument, never())
            .setIntValue(same(userClassReference), any(String.class), any(Integer.class));
        verify(mockitoOldcore.getSpyXWiki(), never())
            .saveDocument(any(XWikiDocument.class), any(String.class), same(mockitoOldcore.getXWikiContext()));

        user.setEmailChecked(false, mockitoOldcore.getXWikiContext());
        verify(userDocument, never())
            .setIntValue(same(userClassReference), any(String.class), any(Integer.class));
        verify(mockitoOldcore.getSpyXWiki(), never())
            .saveDocument(any(XWikiDocument.class), any(String.class), same(mockitoOldcore.getXWikiContext()));

        // With superadmin user we never save anything
        user = new XWikiUser(XWikiRightService.SUPERADMIN_USER_FULLNAME);
        user.setEmailChecked(true, mockitoOldcore.getXWikiContext());
        verify(userDocument, never())
            .setIntValue(same(userClassReference), any(String.class), any(Integer.class));
        verify(mockitoOldcore.getSpyXWiki(), never())
            .saveDocument(any(XWikiDocument.class), any(String.class), same(mockitoOldcore.getXWikiContext()));

        user.setEmailChecked(false, mockitoOldcore.getXWikiContext());
        verify(userDocument, never())
            .setIntValue(same(userClassReference), any(String.class), any(Integer.class));
        verify(mockitoOldcore.getSpyXWiki(), never())
            .saveDocument(any(XWikiDocument.class), any(String.class), same(mockitoOldcore.getXWikiContext()));
    }