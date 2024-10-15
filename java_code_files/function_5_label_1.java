    void testEq1ConfigClassExistsNewInvitationConfig() throws Exception
    {
        // Create an InvitationCommon file.
        XWikiDocument doc = this.xwiki.getDocument(
            new DocumentReference("xwiki", "<script>console.log</script>", "InvitationCommon"), this.context);
        this.xwiki.saveDocument(doc, this.context);
        this.context.setDoc(doc);

        // Create a WebHome file in the same space as InvitationCommon, with an XClass containing the expected fields.
        DocumentReference configDocumentReference =
            new DocumentReference("xwiki", "<script>console.log</script>", "WebHome");
        XWikiDocument configDoc = this.xwiki.getDocument(configDocumentReference, this.context);
        configDoc.getXClass().addTextField("from_address", "From Address", 30);
        // Initialize 8 fields, because that's the only check performed in the document currently.
        IntStream.range(0, 8)
            .forEach(value -> configDoc.getXClass().addTextField("field" + value, "Field " + value, 30));
        this.xwiki.saveDocument(configDoc, this.context);

        // Initialize the document in the same space as InvitationCommon, containing an XObject with a WebHome XObject.
        XWikiDocument hopefullyNonExistantSpaceDoc = this.xwiki.getDocument(
            new DocumentReference("xwiki", "<script>console.log</script>", "HopefullyNonexistantSpace"), this.context);
        BaseObject invitationConfigXObject =
            hopefullyNonExistantSpaceDoc.newXObject(configDocumentReference, this.context);
        invitationConfigXObject.set("from_address", "no-reply@localhost.localdomain", this.context);
        IntStream.range(0, 8)
            .forEach(value -> invitationConfigXObject.set("field" + value, "value " + value, this.context));
        this.xwiki.saveDocument(hopefullyNonExistantSpaceDoc, this.context);

        this.request.put("test", "1");

        DocumentReference reference = new DocumentReference("xwiki", "Invitation", "InvitationCommon");
        XWikiDocument invitationCommonDoc = loadPage(reference);
        Document document = Jsoup.parse(invitationCommonDoc.getRenderedContent(this.context));

        assertEquals("testLoadInvitationConfig", document.selectFirst(".infomessage").text());
        Element firstErrorMessage = document.selectFirst(".errormessage");
        assertEquals("Config document not created", firstErrorMessage.text());
    }