    public void setEmailCheckedFalseNormalUser() throws XWikiException
    {
        XWikiUser user = new XWikiUser(this.userReference);
        user.setEmailChecked(false, this.mockitoOldcore.getXWikiContext());
        verify(this.userDocument, times(1)).setIntValue(this.userClassReference, XWikiUser.EMAIL_CHECKED_PROPERTY, 0);
        verify(this.mockitoOldcore.getSpyXWiki(), times(1))
            .saveDocument(same(this.userDocument), any(String.class), same(this.mockitoOldcore.getXWikiContext()));
    }