    private static void restoreFullZip(int index, File worldFile) {
        byte[] buffer = new byte[1024];
        //The most basic of the bunch.
        ZipEntry entry;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileNames.get(index));
            ZipInputStream zip = new ZipInputStream(fileInputStream);
            while ((entry = zip.getNextEntry()) != null) {
                File outputFile;

                outputFile = new File(worldFile, entry.getName());
                
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }

                CLIIOHelpers.info("Restoring " + outputFile.getName());

                FileOutputStream outputStream = new FileOutputStream(outputFile);
                int length = 0;
                while ((length = zip.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }