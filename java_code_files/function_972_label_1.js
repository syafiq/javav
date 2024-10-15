    public void validDirectoryFileTest() throws IOException, BrutException {
        String validFilename = BrutIO.sanitizeUnknownFile(sTmpDir, "dir" + File.separator + "file");
        assertEquals("dir" + File.separator + "file", validFilename);
    }