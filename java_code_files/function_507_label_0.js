    void testSymlinkZip() throws Exception {
        ZipArchiver archiver = (ZipArchiver) lookup(Archiver.class, "zip");

        File dummyContent = getTestFile("src/test/resources/symlinks/src");
        archiver.addDirectory(dummyContent);
        final File archiveFile = new File("target/output/symlinks.zip");
        archiveFile.delete();
        archiver.setDestFile(archiveFile);
        archiver.createArchive();

        File output = getTestFile("target/output/unzippedSymlinks");
        output.mkdirs();
        ZipUnArchiver unarchiver = (ZipUnArchiver) lookup(UnArchiver.class, "zip");
        unarchiver.setSourceFile(archiveFile);
        unarchiver.setDestFile(output);
        unarchiver.extract();
        // second unpacking should also work
        unarchiver.extract();
    }