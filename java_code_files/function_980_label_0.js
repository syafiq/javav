    public static void beforeClass() throws Exception {
        TestUtils.cleanFrameworkFile();
        sTmpDir = new ExtFile(OS.createTempDirectory());
        TestUtils.copyResourceDir(ResourceDirectoryTraversalTest.class, "decode/arbitrary-write/", sTmpDir);
        Assume.assumeFalse(OSDetection.isWindows());
    }