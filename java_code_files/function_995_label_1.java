    public void setDisabledFalseNormalUser() throws XWikiException
    {
        XWikiUser user = new XWikiUser(userReference);
        user.setDisabled(false, mockitoOldcore.getXWikiContext());
        verify(userDocument, times(1)).setIntValue(userClassReference, XWikiUser.ACTIVE_PROPERTY, 1);
        verify(mockitoOldcore.getSpyXWiki(), times(1))
            .saveDocument(same(userDocument), any(String.class), same(mockitoOldcore.getXWikiContext()));
    }