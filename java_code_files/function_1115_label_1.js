    private static String backupExistingWorld(File worldDir, boolean export) {
        File out = new File(worldDir, "../cli" + serialiseBackupName(export ? "export" : "backup") + ".zip");
        try {
            FileOutputStream outputStream = new FileOutputStream(out);
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            zipOutputStream.setLevel(4);
            Files.walkFileTree(worldDir.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    Path targetFile;
                    try {
                        targetFile = worldDir.toPath().relativize(file);
                        if (targetFile.toFile().getName().compareTo("session.lock") == 0) {
                            return FileVisitResult.CONTINUE;
                        }
                        zipOutputStream.putNextEntry(new ZipEntry(targetFile.toString()));
                        byte[] bytes = Files.readAllBytes(file);
                        zipOutputStream.write(bytes, 0, bytes.length);
                        zipOutputStream.closeEntry();

                    } catch (IOException e) {
                        // TODO : Scream at user
                        e.printStackTrace();
                    }
                    
                        return FileVisitResult.CONTINUE;
                }
            });
            zipOutputStream.flush();
            zipOutputStream.close();

            CLIIOHelpers.info("Done.");
        } catch (Exception e) {
            
        }

        return out.getName();
    }