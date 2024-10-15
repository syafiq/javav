    private static void restoreFolder(int index, File worldFile) {
        File backup = new File(fileNames.get(index));

        try {
            Files.walkFileTree(backup.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    File source = backup.toPath().relativize(file).toFile();
                    File outputFile = new File(worldFile, source.getPath());

                    if (!outputFile.getParentFile().exists()) {
                        outputFile.getParentFile().mkdirs();
                    }
                    
                    CLIIOHelpers.info("Restoring " + outputFile.getName());
                    Files.copy(file, outputFile.toPath());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }