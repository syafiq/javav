    public void invalidBackwardPathOnWindows() throws IOException, BrutException {
        String invalidPath;
        if (! OSDetection.isWindows()) {
            invalidPath = "../../app";
        } else {
            invalidPath = "..\\..\\app.exe";
        }

        BrutIO.sanitizeFilepath(sTmpDir, invalidPath);
    }