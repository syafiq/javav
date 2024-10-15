    public XHTMLOfficeDocument build(InputStream officeFileStream, String officeFileName, DocumentReference reference,
        boolean filterStyles) throws OfficeImporterException
    {
        // Accents seems to cause issues in some conditions
        // See https://jira.xwiki.org/browse/XWIKI-14692
        String cleanedOfficeFileName = StringUtils.stripAccents(officeFileName);

        // Invoke the office document converter.
        Map<String, InputStream> inputStreams = new HashMap<String, InputStream>();
        inputStreams.put(cleanedOfficeFileName, officeFileStream);
        // The office converter uses the output file name extension to determine the output format/syntax.
        String outputFileName = StringUtils.substringBeforeLast(cleanedOfficeFileName, ".") + ".html";
        OfficeConverterResult officeConverterResult;
        try {
            officeConverterResult =
                this.officeServer.getConverter().convertDocument(inputStreams, cleanedOfficeFileName, outputFileName);
        } catch (OfficeConverterException ex) {
            String message = "Error while converting document [%s] into html.";
            throw new OfficeImporterException(String.format(message, officeFileName), ex);
        }

        Document xhtmlDoc = this.cleanAndCreateFile(reference, filterStyles, officeConverterResult);
        Set<File> artifacts = this.handleArtifacts(xhtmlDoc, officeConverterResult);

        // Return a new XHTMLOfficeDocument instance.
        return new XHTMLOfficeDocument(xhtmlDoc, artifacts, officeConverterResult);
    }