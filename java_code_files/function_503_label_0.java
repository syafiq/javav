    void testNonExistingSymlink() throws Exception {
        File zipFile = new File("src/test/resources/symlinks/non_existing_symlink.zip");
        ZipUnArchiver unArchiver = getZipUnArchiver(zipFile);
        String tmpdir = Files.createTempDirectory("tmpe_extract").toFile().getAbsolutePath();
        unArchiver.setDestDirectory(new File(tmpdir));
        ArchiverException exception = assertThrows(ArchiverException.class, unArchiver::extract);
        assertEquals("Entry is outside of the target directory (entry1)", exception.getMessage());
    }