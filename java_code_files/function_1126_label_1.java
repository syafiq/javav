    private static void addBackupNamesToLists(File file, HashMap<String, ZipFile> entryOwners, HashMap<String, Object> filePaths, 
        HashMap<String, String> dates, String colour) throws IOException {
        
        if (file.isFile()) {
            
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entryEnum = zipFile.entries();

            while (entryEnum.hasMoreElements()) {
                ZipEntry entry = entryEnum.nextElement();

                String backupName = file.toString().replace("\\", "/");
                filePaths.put(entry.toString().replace("\\", "/"), entry);
                dates.put(entry.toString().replace("\\", "/"), "\u001b[31m"
                 + backupName
                .substring(backupName.toString().lastIndexOf("/") + 1) 
                //.replace(worldPath + "_", "")
                .replace("backup_", "")
                .replace("-full.zip", "")
                .replace("-partial.zip", "")
                + "\u001B[0m");
                entryOwners.put(entry.toString(), zipFile);
            }
        }

        else {
            Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
                    String backupName = file.toString().replace("\\", "/");
                    filePaths.put(file.toPath().relativize(path).toString().replace("\\", "/"), path);
                    dates.put(file.toPath().relativize(path).toString().replace("\\", "/"), "\u001b[31m"
                     + backupName
                    .substring(backupName.toString().lastIndexOf("/") + 1) 
                    //.replace(worldPath + "_", "")
                    .replace("backup_", "")
                    .replace("-full", "")
                    .replace("-partial", "")
                    + "\u001B[0m");
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }