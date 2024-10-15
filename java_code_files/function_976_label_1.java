    public void invalidRootFileTest() throws IOException, BrutException {
        String rootLocation = OSDetection.isWindows() ? "C:/" : File.separator;
        BrutIO.sanitizeUnknownFile(sTmpDir, rootLocation + "file");
    }