    public void setEmailCheckedTrueNormalUser() throws XWikiException
    {
        XWikiUser user = new XWikiUser(userReference);
        user.setEmailChecked(true, mockitoOldcore.getXWikiContext());
        verify(userDocument, times(1)).setIntValue(userClassReference, XWikiUser.EMAIL_CHECKED_PROPERTY, 1);
        verify(mockitoOldcore.getSpyXWiki(), times(1))
            .saveDocument(same(userDocument), any(String.class), same(mockitoOldcore.getXWikiContext()));
    }