    void testSymlinkTar() throws Exception {
        TarArchiver archiver = (TarArchiver) lookup(Archiver.class, "tar");
        archiver.setLongfile(TarLongFileMode.posix);

        File dummyContent = getTestFile("src/test/resources/symlinks/src");
        archiver.addDirectory(dummyContent);
        final File archiveFile = new File("target/output/symlinks.tar");
        archiver.setDestFile(archiveFile);
        archiver.createArchive();
        File output = getTestFile("target/output/untaredSymlinks");
        output.mkdirs();
        TarUnArchiver unarchiver = (TarUnArchiver) lookup(UnArchiver.class, "tar");
        unarchiver.setSourceFile(archiveFile);
        unarchiver.setDestFile(output);
        unarchiver.extract();
        // second unpacking should also work
        unarchiver.extract();
    }