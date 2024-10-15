    private static XWikiDocument ensureContentAuthorIsMetadataAuthor(XWikiDocument sdoc)
    {
        XWikiDocument result;

        // Make sure the right author is used to execute the textarea
        // Clone the document to avoid changing the cached document instance
        if (!Objects.equals(sdoc.getAuthors().getEffectiveMetadataAuthor(), sdoc.getAuthors().getContentAuthor())) {
            result = sdoc.clone();
            result.getAuthors().setContentAuthor(sdoc.getAuthors().getEffectiveMetadataAuthor());
        } else {
            result = sdoc;
        }

        return result;
    }