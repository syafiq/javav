    void invalidCharacters() throws IOException
    {
        OfficeConverterFileStorage storage =
            new OfficeConverterFileStorage(this.tmpDir, "{/in\\§ä.ext", "{/out\\$ä.out");

        assertEquals("{_in_§a.ext", storage.getInputFile().getName());
        assertEquals("{_out_$a.out", storage.getOutputFile().getName());
        assertEquals(storage.getInputDir(), storage.getInputFile().getParentFile());
        assertEquals(storage.getOutputDir(), storage.getOutputFile().getParentFile());

        assertTrue(storage.getInputDir().isDirectory());
        assertTrue(storage.getOutputDir().isDirectory());
    }