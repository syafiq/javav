    void longFilename() throws IOException
    {
        String longName = " ".repeat(300) + ".ext";
        OfficeConverterFileStorage storage = new OfficeConverterFileStorage(this.tmpDir, longName, longName);

        String expectedName = " ".repeat(251) + ".ext";
        assertEquals(expectedName, storage.getInputFile().getName());
        assertEquals(expectedName, storage.getOutputFile().getName());
    }