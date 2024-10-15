    void testEq0() throws Exception
    {
        this.context.setDoc(this.xwiki.getDocument(
            new DocumentReference("xwiki", "]]  {{noscript/}}", "InvitationCommon"), this.context));

        Document document = Jsoup.parse(loadPage(INVITATION_COMMON_REFERENCE).getRenderedContent(this.context));
        assertEquals("xe.invitation.internalDocument []] {{noscript/}}.WebHome]",
            document.selectFirst(".infomessage").text());
    }