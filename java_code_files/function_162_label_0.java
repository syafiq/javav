    public XDOMOfficeDocument build(XHTMLOfficeDocument xhtmlOfficeDocument) throws OfficeImporterException
    {
        Document xhtmlDoc = xhtmlOfficeDocument.getContentDocument();
        HTMLUtils.stripHTMLEnvelope(xhtmlDoc);
        XDOM xdom = null;
        try {
            xdom = this.xHtmlParser.parse(new StringReader(HTMLUtils.toString(xhtmlDoc)));
        } catch (ParseException ex) {
            throw new OfficeImporterException("Error: Could not parse xhtml office content.", ex);
        }
        return new XDOMOfficeDocument(xdom, xhtmlOfficeDocument.getArtifactsMap(), this.componentManager,
            xhtmlOfficeDocument.getConverterResult());
    }