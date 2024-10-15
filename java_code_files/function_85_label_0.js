    void tofromXMLDocument() throws XWikiException
    {
        // equals won't work on password fields because of https://jira.xwiki.org/browse/XWIKI-12561
        this.baseClass.removeField("passwd");
        this.baseObject.removeField("passwd");
        this.baseObject2.removeField("passwd");
        this.oldcore.getSpyXWiki().saveDocument(this.document, "", true, this.oldcore.getXWikiContext());

        Document document = this.document.toXMLDocument(this.oldcore.getXWikiContext());

        XWikiDocument newDocument = new XWikiDocument(this.document.getDocumentReference());
        newDocument.fromXML(document, false);

        assertEquals(this.document, newDocument);
        // Assert that the document restored from XML is restricted in contrast to the original document.
        assertFalse(this.document.isRestricted());
        assertTrue(newDocument.isRestricted());
    }