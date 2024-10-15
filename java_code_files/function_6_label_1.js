    void testEq1ConfigClassExistsInvalidFromAddress() throws Exception
    {
        // Create an InvitationCommon file.
        DocumentReference documentReference =
            new DocumentReference("xwiki", "<script>console.log</script>", "InvitationCommon");
        XWikiDocument doc = this.xwiki.getDocument(documentReference, this.context);
        this.xwiki.saveDocument(doc, this.context);
        this.context.setDoc(doc);

        // Create a WebHome file in the same space as InvitationCommon, with an XClass containing the expected fields.
        DocumentReference configDocumentReference =
            new DocumentReference("xwiki", "<script>console.log</script>", "WebHome");
        XWikiDocument configDoc = this.xwiki.getDocument(configDocumentReference, this.context);
        configDoc.getXClass().addTextField("from_address", "From Address", 30);
        this.xwiki.saveDocument(configDoc, this.context);

        // Initialize the document in the same space as InvitationCommon, containing an XObject with a WebHome XObject.
        XWikiDocument hopefullyNonExistantSpaceDoc = this.xwiki.getDocument(
            new DocumentReference("xwiki", "<script>console.log</script>", "HopefullyNonexistantSpace"), this.context);
        BaseObject invitationConfigXObject =
            hopefullyNonExistantSpaceDoc.newXObject(configDocumentReference, this.context);
        invitationConfigXObject.set("from_address", "<script>console.log('from_address')</script>", this.context);
        this.xwiki.saveDocument(hopefullyNonExistantSpaceDoc, this.context);

        this.request.put("test", "1");

        XWikiDocument invitationCommonDoc = loadPage(new DocumentReference("xwiki", "Invitation", "InvitationCommon"));
        Document document = Jsoup.parse(invitationCommonDoc.getRenderedContent(this.context));

        assertEquals("testLoadInvitationConfig", document.selectFirst(".infomessage").text());
        Element firstErrorMessage = document.selectFirst(".errormessage");
        assertEquals("Config map too small", firstErrorMessage.text());
        Element secondErrorMessage = document.select(".errormessage").get(1);
        assertEquals("form_address incorrect, expecting \"no-reply@localhost.localdomain\" "
            + "got \"<script>console.log('from_address')</script>\"", secondErrorMessage.text());
    }