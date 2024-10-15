    public static void extractZip(File file, File destDirectory, ExtractTaskListener listener) throws IOException {
        if (!destDirectory.exists()) {
            destDirectory.mkdir();
        }

        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
            listener.onMessage("Extracting resource pack " + file.getName() + " to directory " + destDirectory.getName());
            int elementCount = 0;
            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = zipEntries.nextElement();
                if (zipEntry.getName().contains("..")) {
                    listener.onMessage("Skipping " + zipEntry.getName() + ": Invalid path");
                    continue;
                }

                try {
                    listener.onMessage("Extracting " + zipEntry.getName());
                    try (InputStream entryInputStream = zipFile.getInputStream(zipEntry)) {
                        String filePath = destDirectory + File.separator + zipEntry.getName();
                        File zipEntryFile = new File(filePath);
                        zipEntryFile.getParentFile().mkdirs();
                        listener.fileProcessed(entryInputStream.available());
                        extractFile(entryInputStream, zipEntryFile);
                        elementCount++;
                    }
                } catch (IOException e) {
                    if (!zipEntry.isDirectory()) {
                        listener.onMessage("Skipping " + zipEntry.getName() + ": " + e.getMessage());
                    }
                }
            }

            listener.onMessage("Successfully extracted " + elementCount + " elements");
            listener.taskCompleted();
        }
    }