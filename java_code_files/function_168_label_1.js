    protected OfficeConverterResult importPresentation(InputStream officeFileStream, String officeFileName)
        throws OfficeImporterException
    {
        Map<String, InputStream> inputStreams = new HashMap<String, InputStream>();
        inputStreams.put(officeFileName, officeFileStream);
        try {
            // The office converter uses the output file name extension to determine the output format/syntax.
            // The returned artifacts are of three types: imgX.jpg (slide screen shot), imgX.html (HTML page that
            // display the corresponding slide screen shot) and textX.html (HTML page that display the text extracted
            // from the corresponding slide). We use "img0.html" as the output file name because the corresponding
            // artifact displays a screen shot of the first presentation slide.
            return this.officeServer.getConverter().convertDocument(inputStreams, officeFileName, "img0.html");
        } catch (OfficeConverterException e) {
            String message = "Error while converting document [%s] into html.";
            throw new OfficeImporterException(String.format(message, officeFileName), e);
        }
    }