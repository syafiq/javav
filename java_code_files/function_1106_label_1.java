    private static void restorePartialIncremental(int index, File worldFile) {
        //Do we need to check for past backups? if selected is a full backup, we do not.
        HashMap<String, Object> filePaths = new HashMap<>();
        HashMap<String, String> dates = new HashMap<>();
        HashMap<String, ZipFile> entryOwners = new HashMap<>();
        try {
            File backup = new File(fileNames.get(index));
            if (!backup.getName().contains("-full")) {
                int i;
                //find last FULL backup
                for (i = index;i>=0;i--) {
                    String name = fileNames.get(i);
                    if (name.contains("-full")) {
                        addBackupNamesToLists(new File(name), entryOwners, filePaths, dates, "\u001b[31m");
                        break;
                    }    
                }
                while (i < index) {
                    String name = fileNames.get(i);
                    addBackupNamesToLists(new File(name), entryOwners, filePaths, dates, "\u001b[31m");
                    i++;
                }
                
            }

            File file = new File(fileNames.get(index));
            addBackupNamesToLists(file, entryOwners, filePaths, dates, "\u001B[32m");

            HashMap<String, Object> properMapping = new HashMap<>();
            for (String date : dates.keySet()) {
                properMapping.put(
                    date + " " + dates.get(date),
                    filePaths.get(date)
                );
            }

            Object select = CLIIOHelpers.getFileToRestore(properMapping, "", worldFile);
            if (select instanceof Path) {
                Path input = (Path) select;


                if (select.toString().replace("\\", "/").contains("-full/")) {
                    select = new File(
                        select.toString().replace("\\", "/")
                        .split("-full/")[1]
                    ).toPath();
                }
                if (select.toString().replace("\\", "/").contains("-partial/")) {
                    select = new File(
                        select.toString().replace("\\", "/")
                        .split("-partial/")[1]
                    ).toPath();
                }
    
                File outputFile = new File(worldFile, select.toString());
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }
                CLIIOHelpers.info("\n\nRestoring file : " + select);
                Files.copy(input, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            else if (select instanceof ZipEntry) {
                ZipEntry entry = (ZipEntry) select;

                File outputFile = new File(worldFile, entry.toString());
                FileOutputStream outputStream = new FileOutputStream(outputFile);

                CLIIOHelpers.info("Restoring " + entry.toString() + "...");

                byte[] buffer = new byte[1028];
                InputStream inputSteam = entryOwners.get(entry.toString()).getInputStream(entry);
                int length;
                while ((length = inputSteam.read(buffer, 0, buffer.length)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
            }


        }
        catch (IOException e){
            //TODO : Scream at user
            e.printStackTrace();
        }
    }