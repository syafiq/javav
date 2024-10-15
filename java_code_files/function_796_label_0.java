    public void zipSlip() throws IOException {
        File zipFile = createMaliciousZipFile();
        Path destination = Files.createTempDirectory("zipSlip");

        Unzip unzip = new Unzip();
        unzip.setSource(zipFile);
        unzip.setDestination(destination.toFile());

        Exception exception = assertThrows(ZipException.class, unzip::extract);
        assertTrue(exception.getMessage().contains("is trying to leave the target output directory"));
    }