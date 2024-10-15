    public void isDisabled()
    {
        XWikiUser user = new XWikiUser(this.userReference);
        when(this.userDocument.getIntValue(this.userClassReference, XWikiUser.ACTIVE_PROPERTY, 1)).thenReturn(1);
        assertFalse(user.isDisabled(this.mockitoOldcore.getXWikiContext()));

        when(this.userDocument.getIntValue(this.userClassReference, XWikiUser.ACTIVE_PROPERTY, 1)).thenReturn(0);
        assertTrue(user.isDisabled(this.mockitoOldcore.getXWikiContext()));

        user = new XWikiUser((DocumentReference) null);
        assertFalse(user.isDisabled(this.mockitoOldcore.getXWikiContext()));

        user = new XWikiUser(XWikiRightService.SUPERADMIN_USER_FULLNAME);
        assertFalse(user.isDisabled(this.mockitoOldcore.getXWikiContext()));
    }