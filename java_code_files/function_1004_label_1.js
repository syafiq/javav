    public void isEmailChecked()
    {
        XWikiUser user = new XWikiUser(userReference);
        when(userDocument.getIntValue(userClassReference, XWikiUser.EMAIL_CHECKED_PROPERTY, 1)).thenReturn(1);
        assertTrue(user.isEmailChecked(mockitoOldcore.getXWikiContext()));

        when(userDocument.getIntValue(userClassReference, XWikiUser.EMAIL_CHECKED_PROPERTY, 1)).thenReturn(0);
        assertFalse(user.isEmailChecked(mockitoOldcore.getXWikiContext()));

        user = new XWikiUser((DocumentReference) null);
        assertTrue(user.isEmailChecked(mockitoOldcore.getXWikiContext()));

        user = new XWikiUser(XWikiRightService.SUPERADMIN_USER_FULLNAME);
        assertTrue(user.isEmailChecked(mockitoOldcore.getXWikiContext()));
    }