    void escapeInfoMessageInternalDocumentParameter() throws Exception
    {
        XWikiDocument invitationGuestActionsDocument = loadPage(INVITATION_CONFIG_REFERENCE);

        // Set up the current doc in the context so that $doc is bound in scripts
        this.context.setDoc(
            this.xwiki.getDocument(new DocumentReference("xwiki", "]] {{noscript/}}", "Page"), this.context));

        Document document = Jsoup.parse(invitationGuestActionsDocument.getRenderedContent(this.context));
        Element infomessage = document.selectFirst(".infomessage");
        assertEquals("xe.invitation.internalDocument [Invitation.WebHome]", infomessage.text());
    }