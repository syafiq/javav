    public void filter(Document htmlDocument, Map<String, String> cleaningParams)
    {
        String targetDocumentName = cleaningParams.get("targetDocument");
        DocumentReference targetDocumentReference =
            targetDocumentName == null ? null : this.stringDocumentReferenceResolver.resolve(targetDocumentName);

        boolean attachEmbeddedImages = Boolean.valueOf(cleaningParams.get("attachEmbeddedImages"));
        if (attachEmbeddedImages) {
            htmlDocument.setUserData(EMBEDDED_IMAGES, new HashMap<String, byte[]>(), null);
        }

        List<Element> images = filterDescendants(htmlDocument.getDocumentElement(), new String[] {TAG_IMG});
        for (Element image : images) {
            Attr source = image.getAttributeNode(ATTRIBUTE_SRC);
            if (source != null && targetDocumentReference != null) {
                filterImageSource(source, targetDocumentReference);
            }

            // The 'align' attribute of images creates a lot of problems. First,the office server has a problem with
            // center aligning images (it aligns them to left). Next, the office server uses <br clear"xxx"> for
            // avoiding content wrapping around images which is not valid XHTML. There for, to be consistent and simple
            // we will remove the 'align' attribute of all the images so that they are all left aligned.
            image.removeAttribute(ATTRIBUTE_ALIGN);
        }
    }