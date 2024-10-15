    public DefaultOfficeConverterResult convertDocument(Map<String, InputStream> inputStreams, String inputFileName,
        String outputFileName) throws OfficeConverterException
    {
        this.checkInputStream(inputStreams, inputFileName);

        try {
            // Prepare temporary storage.
            OfficeConverterFileStorage storage = new OfficeConverterFileStorage(this.workDir, inputFileName,
                outputFileName);

            // Check that the potentially cleaned filename is actually in the input streams.
            this.checkInputStream(inputStreams, storage.getInputFile().getName());

            // Write out all the input streams.
            for (Map.Entry<String, InputStream> entry : inputStreams.entrySet()) {
                File temp = new File(storage.getInputDir(), entry.getKey());
                try (FileOutputStream fos = new FileOutputStream(temp)) {
                    IOUtils.copy(entry.getValue(), fos);
                }
            }

            // Perform the conversion.
            this.converter.convert(storage.getInputFile())
                .to(storage.getOutputFile())
                .execute();

            return new DefaultOfficeConverterResult(storage);
        } catch (Exception ex) {
            throw new OfficeConverterException(CONVERSION_ERROR_MESSAGE, ex);
        }
    }