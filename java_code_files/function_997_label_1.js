    public void setEmailCheckedFalseNormalUser() throws XWikiException
    {
        XWikiUser user = new XWikiUser(userReference);
        user.setEmailChecked(false, mockitoOldcore.getXWikiContext());
        verify(userDocument, times(1)).setIntValue(userClassReference, XWikiUser.EMAIL_CHECKED_PROPERTY, 0);
        verify(mockitoOldcore.getSpyXWiki(), times(1))
            .saveDocument(same(userDocument), any(String.class), same(mockitoOldcore.getXWikiContext()));
    }