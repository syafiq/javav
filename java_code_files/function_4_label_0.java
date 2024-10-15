    void testEq1ConfigClassIsNew() throws Exception
    {
        String spaceName = "<script>console.log</script>]]{{noscript/}}";

        XWikiDocument doc =
            this.xwiki.getDocument(new DocumentReference("xwiki", spaceName, "InvitationCommon"), this.context);
        this.xwiki.saveDocument(doc, this.context);
        this.context.setDoc(doc);

        this.request.put("test", "1");

        Document document = Jsoup.parse(loadPage(INVITATION_COMMON_REFERENCE).getRenderedContent(this.context));

        assertEquals("testLoadInvitationConfig", document.selectFirst(".infomessage").text());
        Element errorMessage = document.selectFirst(".errormessage");
        assertEquals("Class document <script>console\\.log</script>]]{{noscript/}}.WebHome not found. "
            + "can't run test.", errorMessage.text());
        Element errorLink = errorMessage.selectFirst("a");
        assertEquals("<script>console\\.log</script>]]{{noscript/}}.WebHome", errorLink.text());
        assertEquals("<script>console\\.log</script>]]{{noscript/}}.WebHome", errorLink.attr("href"));
    }