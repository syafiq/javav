    public void setDisabledTrueNormalUser() throws XWikiException
    {
        XWikiUser user = new XWikiUser(userReference);
        user.setDisabled(true, mockitoOldcore.getXWikiContext());
        verify(userDocument, times(1)).setIntValue(userClassReference, XWikiUser.ACTIVE_PROPERTY, 0);
        verify(mockitoOldcore.getSpyXWiki(), times(1))
            .saveDocument(same(userDocument), any(String.class), same(mockitoOldcore.getXWikiContext()));
    }