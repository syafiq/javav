    public void isDisabled()
    {
        XWikiUser user = new XWikiUser(userReference);
        when(userDocument.getIntValue(userClassReference, XWikiUser.ACTIVE_PROPERTY, 1)).thenReturn(1);
        assertFalse(user.isDisabled(mockitoOldcore.getXWikiContext()));

        when(userDocument.getIntValue(userClassReference, XWikiUser.ACTIVE_PROPERTY, 1)).thenReturn(0);
        assertTrue(user.isDisabled(mockitoOldcore.getXWikiContext()));

        user = new XWikiUser((DocumentReference) null);
        assertFalse(user.isDisabled(mockitoOldcore.getXWikiContext()));

        user = new XWikiUser(XWikiRightService.SUPERADMIN_USER_FULLNAME);
        assertFalse(user.isDisabled(mockitoOldcore.getXWikiContext()));
    }