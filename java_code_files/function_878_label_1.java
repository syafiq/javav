    public List<WikiComponent> buildComponents(BaseObject baseObject) throws WikiComponentException
    {
        // Empty extension point id is invalid UIX
        String extensionPointId = baseObject.getStringValue(EXTENSION_POINT_ID_PROPERTY);

        if (StringUtils.isEmpty(extensionPointId)) {
            // TODO: put back when we stop using this has as a feature
            // throw new WikiComponentException("Invalid UI extension: non empty extension point id is required");

            return Collections.emptyList();
        }

        WikiComponentScope scope = WikiComponentScope.fromString(baseObject.getStringValue(SCOPE_PROPERTY));

        XWikiDocument ownerDocument = baseObject.getOwnerDocument();

        // Before going further we need to check the document author is authorized to register the extension
        checkRights(ownerDocument, scope);

        // Extract extension definition.
        String id = baseObject.getStringValue(ID_PROPERTY);

        String roleHint = this.serializer.serialize(baseObject.getReference());

        WikiUIExtension extension;
        try {
            extension = this.extensionProvider.get();
            extension.initialize(baseObject, roleHint, id, extensionPointId);
        } catch (Exception e) {
            throw new WikiComponentException(
                String.format("Failed to initialize Panel UI extension [%s]", baseObject.getReference()), e);
        }

        String rawParameters = baseObject.getStringValue(PARAMETERS_PROPERTY);

        // It would be nice to have PER_LOOKUP components for UIX parameters but without constructor injection it's
        // safer to use a POJO and pass the Component Manager to it.
        WikiUIExtensionParameters parameters =
            new WikiUIExtensionParameters(id, rawParameters, this.wikiComponentManager);
        extension.setParameters(parameters);
        extension.setScope(scope);

        return Collections.singletonList(extension);
    }