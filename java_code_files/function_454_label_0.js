    public void testUntarZipSlip() throws IOException {
        FileProjectManager manager = new FileProjectManagerStub(workspaceDir);

        File tempDir = TestUtils.createTempDirectory("openrefine-project-import-zip-slip-test");
        try {
            File subDir = new File(tempDir, "dest");
            InputStream stream = FileProjectManagerTests.class.getClassLoader().getResourceAsStream("zip-slip.tar");

            assertThrows(IllegalArgumentException.class, () -> manager.untar(subDir, stream));
        } finally {
            tempDir.delete();
        }
    }