    public Map<TargetDocumentDescriptor, XDOMOfficeDocument> split(XDOMOfficeDocument officeDocument,
        OfficeDocumentSplitterParameters parameters) throws OfficeImporterException
    {
        ComponentManager componentManager = this.componentManagerProvider.get();
        Map<TargetDocumentDescriptor, XDOMOfficeDocument> result =
            new HashMap<TargetDocumentDescriptor, XDOMOfficeDocument>();

        // Create splitting and naming criterion for refactoring.
        SplittingCriterion splittingCriterion =
            new HeadingLevelSplittingCriterion(parameters.getHeadingLevelsToSplit());
        NamingCriterion namingCriterion;
        try {
            namingCriterion = componentManager.getInstance(NamingCriterion.class, parameters.getNamingCriterionHint());
        } catch (ComponentLookupException e) {
            throw new OfficeImporterException("Failed to create the naming criterion.", e);
        }
        namingCriterion.getParameters().setBaseDocumentReference(parameters.getBaseDocumentReference());
        namingCriterion.getParameters().setUseTerminalPages(parameters.isUseTerminalPages());

        // Create the root document required by refactoring module.
        WikiDocument rootDoc =
            new WikiDocument(parameters.getBaseDocumentReference(), officeDocument.getContentDocument(), null);
        List<WikiDocument> documents = this.documentSplitter.split(rootDoc, splittingCriterion, namingCriterion);

        for (WikiDocument doc : documents) {
            // Initialize a target page descriptor.
            TargetDocumentDescriptor targetDocumentDescriptor =
                new TargetDocumentDescriptor(doc.getDocumentReference(), componentManager);
            if (doc.getParent() != null) {
                targetDocumentDescriptor.setParentReference(doc.getParent().getDocumentReference());
            }

            // Rewire artifacts.
            Set<File> artifactsFiles = relocateArtifacts(doc, officeDocument);

            // Create the resulting XDOMOfficeDocument.
            XDOMOfficeDocument splitDocument = new XDOMOfficeDocument(doc.getXdom(), artifactsFiles, componentManager,
                officeDocument.getConverterResult());
            result.put(targetDocumentDescriptor, splitDocument);
        }

        return result;
    }