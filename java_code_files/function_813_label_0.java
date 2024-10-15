    public void deleteDocumentVersions(XWikiDocument document, String version1, String version2, XWikiContext context)
        throws XWikiException
    {
        deleteDocumentVersions(document, version1, version2, false, context);
    }