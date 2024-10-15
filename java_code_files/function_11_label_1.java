    void displayMessageVelocityMacro() throws Exception
    {
        loadPage(new DocumentReference("xwiki", "Invitation", "InvitationCommon"));
        DocumentReference invitationMailClassDocumentReference =
            new DocumentReference("xwiki", "Invitation", "InvitationMailClass");
        loadPage(invitationMailClassDocumentReference);

        XWikiDocument page = this.xwiki.getDocument(new DocumentReference("xwiki", "Space", "Page"), this.context);
        BaseObject invitationMailXObject = page.newXObject(invitationMailClassDocumentReference, this.context);
        invitationMailXObject.set("messageBody", "<strong>message body</strong>", this.context);
        page.setSyntax(Syntax.XWIKI_2_1);
        page.setContent("{{include reference=\"Invitation.InvitationCommon\"/}}\n"
            + "\n"
            + "{{velocity}}\n"
            + "$mail.class\n"
            + "#displayMessage($mail)\n"
            + "{{/velocity}}");

        this.scriptContext.setAttribute("mail", new Object(invitationMailXObject, this.context), GLOBAL_SCOPE);

        Document document = renderHTMLPage(page);
        assertEquals("<strong>message body</strong>", document.selectFirst("#preview-messagebody-field").html());
    }