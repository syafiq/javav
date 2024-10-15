    private static void restoreOtherModZip(File backupDir) {
        worldFile = serverDir;
        Path file;
        HashMap<String, File> backups = new HashMap<>();
        HashMap<String, Path> entries = new HashMap<>();

        for (File b : backupDir.getParentFile().listFiles()) {
            if (b.getName().endsWith("zip")) {
                backups.put(b.getName(), b);
            }
        }

        String backupName = CLIIOHelpers.getSelectionFromList("Select a backup to restore from.", new ArrayList<String>(backups.keySet()));
        
        boolean fullWorld = CLIIOHelpers.getSelectionFromList("Do you want to restore the whole world or a singular file?", 
        Arrays.asList(new String[]{"Whole world", "Single file"})) == "Whole world";

        if (!fullWorld) {
            if (!CLIIOHelpers.confirmWarningMessage()) return;
            
            try {
                FileSystem zipFs = FileSystems.newFileSystem(backups.get(backupName).toPath(), AdvancedBackupsCLI.class.getClassLoader());
                Path root = zipFs.getPath("");
                Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                        entries.put(file.toString(), file);
                        return FileVisitResult.CONTINUE;
                    }
                });

                file = CLIIOHelpers.getFileToRestore(entries, "", worldFile);
                CLIIOHelpers.info("Restoring " + file.toString() + "...");
                Path outputFile = new File(worldFile, file.toString()).toPath();
                if (!outputFile.getParent().toFile().exists()) {
                    outputFile.getParent().toFile().mkdirs();
                }
                Files.copy(file, outputFile, StandardCopyOption.REPLACE_EXISTING);
                CLIIOHelpers.info("Done.");
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return;
            }
        }

        else {
            if (!CLIIOHelpers.confirmWarningMessage()) return;

            Path levelDatPath;
            ArrayList<Path> levelDatPathWrapper = new ArrayList<>();
            
            try {
                FileSystem zipFs = FileSystems.newFileSystem(backups.get(backupName).toPath(), AdvancedBackupsCLI.class.getClassLoader());
                Path root = zipFs.getPath("");
                Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                        if (file.getFileName().toString().equals("level.dat")) levelDatPathWrapper.add(file);
                        return FileVisitResult.CONTINUE;
                    }
                });

                levelDatPath = levelDatPathWrapper.get(0);
                CLIIOHelpers.info("Making backup of existing world...");
                CLIIOHelpers.info("Backup saved to : " + deleteEntireWorld(new File(worldFile, levelDatPath.getParent().toString()), false));
                byte[] buffer = new byte[1024];
                ZipEntry entry;
                FileInputStream fileInputStream = new FileInputStream(backups.get(backupName));
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                while ((entry = zip.getNextEntry()) != null) {
                    File outputFile;
    
                    outputFile = new File(worldFile, entry.getName());
                    
                    if (!outputFile.getParentFile().exists()) {
                        outputFile.getParentFile().mkdirs();
                    }

                    
                    CLIIOHelpers.info("Restoring " + outputFile.toString() + "...");
    
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

        CLIIOHelpers.info("Done.");

    }