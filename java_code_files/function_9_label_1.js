    void testEq0() throws Exception
    {
        this.context.setDoc(this.xwiki.getDocument(
            new DocumentReference("xwiki", "]]  {{noscript/}}", "InvitationCommon"), this.context));

        DocumentReference invitationCommonReference = new DocumentReference("xwiki", "Invitation", "InvitationCommon");
        Document document = Jsoup.parse(loadPage(invitationCommonReference).getRenderedContent(this.context));
        assertEquals("xe.invitation.internalDocument []] {{noscript/}}.WebHome]",
            document.selectFirst(".infomessage").text());
    }