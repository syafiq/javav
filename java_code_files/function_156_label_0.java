    public XHTMLOfficeDocument build(InputStream officeFileStream, String officeFileName, DocumentReference reference,
        boolean filterStyles) throws OfficeImporterException
    {
        String inputFileName = OfficeConverterFileStorage.getSafeInputFilenameFromExtension(officeFileName);

        // Invoke the office document converter.
        Map<String, InputStream> inputStreams = new HashMap<>();
        inputStreams.put(inputFileName, officeFileStream);
        // The office converter uses the output file name extension to determine the output format/syntax.
        String outputFileName = "output.html";
        OfficeConverterResult officeConverterResult;
        try {
            officeConverterResult =
                this.officeServer.getConverter().convertDocument(inputStreams, inputFileName, outputFileName);
        } catch (OfficeConverterException ex) {
            String message = "Error while converting document [%s] into html.";
            throw new OfficeImporterException(String.format(message, officeFileName), ex);
        }

        // Replace the prefix "output_html" that JODConverter/LibreOffice prepend based on the output file name by
        // prefix based on the user-provided input name
        String replacePrefix = "output_html_";
        String replacementPrefix = StringUtils.substringBeforeLast(officeFileName, ".") + "_";

        Document xhtmlDoc = this.cleanAndCreateFile(reference, filterStyles, officeConverterResult,
            replacePrefix, replacementPrefix);
        Map<String, OfficeDocumentArtifact> artifacts = this.handleArtifacts(xhtmlDoc, officeConverterResult,
            replacePrefix, replacementPrefix);

        // Return a new XHTMLOfficeDocument instance.
        return new XHTMLOfficeDocument(xhtmlDoc, artifacts, officeConverterResult);
    }