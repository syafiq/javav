    public OfficeConverterFileStorage(File parentDir, String inputFileName, String outputFileName) throws IOException
    {
        boolean success = false;

        // Realize the temporary directory hierarchy.
        this.rootDir = new File(parentDir, UUID.randomUUID().toString());
        if (this.rootDir.mkdir()) {
            this.inputDir = new File(this.rootDir, INPUT);
            this.outputDir = new File(this.rootDir, "output");
            if (this.inputDir.mkdir() && this.outputDir.mkdir()) {
                this.inputFile = new File(this.inputDir, cleanFilename(inputFileName));
                this.outputFile = new File(this.outputDir, cleanFilename(outputFileName));
                success = true;
            }
        }

        // Cleanup & signal if an error is encountered.
        if (!success) {
            cleanUp();
            throw new IOException("Could not create temporary directory hierarchy.");
        }
    }