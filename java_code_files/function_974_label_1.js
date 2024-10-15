    public void validFileTest() throws IOException, BrutException {
        String validFilename = BrutIO.sanitizeUnknownFile(sTmpDir, "file");
        assertEquals(validFilename, "file");

        File validFile = new File(sTmpDir, validFilename);
        assertTrue(validFile.isFile());
    }