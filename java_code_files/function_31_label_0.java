    public List<Block> execute(DisplayIconMacroParameters parameters, String content,
        MacroTransformationContext context) throws MacroExecutionException
    {
        List<Block> result;

        try {
            IconSet iconSet = getIconSet(parameters);

            if (iconSet == null) {
                result = List.of();
            } else {
                XDOM iconBlock = parseIcon(parameters, context, iconSet);

                BlockAsyncRendererConfiguration rendererConfiguration =
                    createBlockAsyncRendererConfiguration(null, iconBlock, null, context);
                rendererConfiguration.setAsyncAllowed(false);
                rendererConfiguration.setCacheAllowed(false);

                if (iconSet.getSourceDocumentReference() != null) {
                    DocumentReference sourceDocumentReference = iconSet.getSourceDocumentReference();

                    DocumentModelBridge sourceDocument =
                        this.documentAccessBridge.getDocumentInstance(sourceDocumentReference);
                    DocumentReference authorReference =
                        this.documentUserSerializer.serialize(sourceDocument.getAuthors().getContentAuthor());

                    rendererConfiguration.setSecureReference(sourceDocumentReference, authorReference);
                    rendererConfiguration.useEntity(sourceDocumentReference);

                    String stringDocumentReference =
                        this.defaultEntityReferenceSerializer.serialize(iconSet.getSourceDocumentReference());
                    rendererConfiguration.setTransformationId(stringDocumentReference);
                    rendererConfiguration.setResricted(false);

                    result = this.documentContextExecutor.call(
                        () -> List.of(this.executor.execute(rendererConfiguration)),
                        sourceDocument
                    );
                } else {
                    result = List.of(this.executor.execute(rendererConfiguration));
                }
            }
        } catch (MacroExecutionException e) {
            throw e;
        } catch (Exception e) {
            throw new MacroExecutionException("Failed parsing and executing the icon.", e);
        }

        return result;
    }