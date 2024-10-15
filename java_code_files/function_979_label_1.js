    public void invalidBackwardFileTest() throws IOException, BrutException {
        BrutIO.sanitizeUnknownFile(sTmpDir, "../file");
    }