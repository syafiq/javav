    private OfficeDocumentView getView(ResourceReference resourceReference, Map<String, ?> parameters) throws Exception
    {
        DocumentReference ownerDocument = getOwnerDocument(parameters);
        String serializedResourceReference = this.resourceReferenceTypeSerializer.serialize(resourceReference);

        // Search the cache.
        String cacheKey = getCacheKey(ownerDocument, serializedResourceReference, parameters);
        OfficeDocumentView view = this.externalCache.get(cacheKey);

        // If a view in not available, build one and cache it.
        if (view == null) {
            try (XDOMOfficeDocument xdomOfficeDocument = createXDOM(ownerDocument, resourceReference, parameters))
            {
                XDOM xdom = xdomOfficeDocument.getContentDocument();
                Set<File> temporaryFiles = processImages(xdom, xdomOfficeDocument.getArtifactsFiles(), ownerDocument,
                    parameters);
                view = new OfficeDocumentView(resourceReference, xdom, temporaryFiles);

                this.externalCache.set(cacheKey, view);
            }
        }

        return view;
    }