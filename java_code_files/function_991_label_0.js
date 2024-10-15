    public static void extractMinecraft(File file, File destDirectory, ExtractTaskListener listener) throws IOException {
        if (!destDirectory.exists()) {
            destDirectory.mkdir();
        }

        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<? extends JarEntry> jarEntries = jarFile.entries();
            listener.onMessage("Extracting resource pack from Minecraft " + file.getName() + " to directory " + destDirectory.getName());
            int elementCount = 0;
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                if (jarEntry.getName().contains("..")) {
                    listener.onMessage("Skipping " + jarEntry.getName() + ": Invalid path");
                    continue;
                }

                if (jarEntry.getName().startsWith("assets/") && !jarEntry.isDirectory()) {
                    listener.onMessage("Extracting " + jarEntry.getName());
                    try (InputStream entryInputStream = jarFile.getInputStream(jarEntry)) {
                        String filePath = destDirectory + File.separator + jarEntry.getName();
                        File jarEntryFile = new File(filePath);
                        jarEntryFile.getParentFile().mkdirs();
                        listener.fileProcessed(entryInputStream.available());
                        extractFile(entryInputStream, jarEntryFile);
                        elementCount++;
                    }
                }
            }

            listener.onMessage("Successfully extracted " + elementCount + " elements");
            listener.taskCompleted();
        }
    }