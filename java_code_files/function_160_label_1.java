    private Document cleanAndCreateFile(DocumentReference reference, boolean filterStyles,
        OfficeConverterResult officeConverterResult) throws OfficeImporterException
    {
        // Prepare the parameters for HTML cleaning.
        Map<String, String> params = new HashMap<String, String>();
        params.put("targetDocument", this.entityReferenceSerializer.serialize(reference));
        // Extract the images that are embedded through the Data URI scheme and add them to the other artifacts so that
        // they end up as attachments.
        params.put("attachEmbeddedImages", "true");
        if (filterStyles) {
            params.put("filterStyles", "strict");
        }

        // Parse and clean the HTML output.
        HTMLCleanerConfiguration configuration = this.officeHtmlCleaner.getDefaultConfiguration();
        configuration.setParameters(params);

        Reader html = null;
        try {
            html = new FileReader(officeConverterResult.getOutputFile());
        } catch (FileNotFoundException e) {
            throw new OfficeImporterException(
                String.format("The output file cannot be found: [%s].", officeConverterResult.getOutputFile()), e);
        }
        return this.officeHtmlCleaner.clean(html, configuration);
    }