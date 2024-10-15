    public XDOMOfficeDocument(XDOM xdom, Set<File> artifactFiles, ComponentManager componentManager,
        OfficeConverterResult converterResult)
    {
        this.xdom = xdom;
        this.artifactFiles = artifactFiles;
        this.componentManager = componentManager;
        this.converterResult = converterResult;
    }