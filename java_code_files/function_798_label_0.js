    private File createMaliciousZipFile() throws IOException {
        File zipFile = File.createTempFile("malicious", ".zip");
        String maliciousFileName = "../malicious.sh";
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            ZipEntry entry = new ZipEntry(maliciousFileName);
            zipOutputStream.putNextEntry(entry);
            zipOutputStream.write("Malicious content".getBytes());
            zipOutputStream.closeEntry();
        }

        return zipFile;
    }