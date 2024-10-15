    protected OfficeConverterResult importPresentation(InputStream officeFileStream, String officeFileName)
        throws OfficeImporterException
    {
        String inputFileName = OfficeConverterFileStorage.getSafeInputFilenameFromExtension(officeFileName);
        Map<String, InputStream> inputStreams = Map.of(inputFileName, officeFileStream);
        try {
            // The office converter uses the output file name extension to determine the output format/syntax.
            // The returned artifacts are of three types: imgX.jpg (slide screen shot), imgX.html (HTML page that
            // display the corresponding slide screen shot) and textX.html (HTML page that display the text extracted
            // from the corresponding slide). We use "img0.html" as the output file name because the corresponding
            // artifact displays a screen shot of the first presentation slide.
            return this.officeServer.getConverter().convertDocument(inputStreams, inputFileName, "img0.html");
        } catch (OfficeConverterException e) {
            String message = "Error while converting document [%s] into html.";
            throw new OfficeImporterException(String.format(message, officeFileName), e);
        }
    }