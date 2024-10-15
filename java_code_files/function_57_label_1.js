    public IconSet loadIconSet(DocumentReference iconSetReference) throws IconException
    {
        try {
            // Get the document
            DocumentModelBridge doc = documentAccessBridge.getDocumentInstance(iconSetReference);
            String content = doc.getContent();
            // The name of the icon set is stored in the IconThemesCode.IconThemeClass XObject of the document
            DocumentReference iconClassRef = new DocumentReference(wikiDescriptorManager.getCurrentWikiId(),
                "IconThemesCode", "IconThemeClass");
            String name = (String) documentAccessBridge.getProperty(iconSetReference, iconClassRef, "name");
            // Load the icon set
            return loadIconSet(new StringReader(content), name);
        } catch (Exception e) {
            throw new IconException(String.format(ERROR_MSG, iconSetReference), e);
        }
    }