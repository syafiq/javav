    private static int getBackupDate(File backupDir, boolean exportMode) throws IOException {
        fileNames.clear();
        int inputType;

        CLIIOHelpers.info("Select a backup to restore.");

        String[] fileNameArray = backupDir.list();
        if (fileNameArray == null || fileNameArray.length <=0) {
            throw new IOException(String.format("Selected backup directory %s is empty, or is a file!", backupDir.getAbsolutePath()));
        }
        ArrayList<String> fileNameList = new ArrayList<String>(Arrays.asList(fileNameArray)); //i need to do this. i hate this.
        fileNameList.removeIf((name) -> {
            return (name.endsWith("json") ||
                name.contains("incomplete") ||
                name.contains("DS_Store"));
        });

        if (fileNameList.isEmpty()) {
            throw new IOException(String.format("Selected backup directory %s is empty, or is a file!", backupDir.getAbsolutePath()));
        }

        for (String fileName : CLIIOHelpers.sortStringsAlphabeticallyWithDirectoryPriority(fileNameList)) {
            if (exportMode) {
                if (fileName.endsWith("json")) continue;
                if (fileName.contains("incomplete")) continue;
                File file = new File(backupDir, fileName);
                fileNames.add(file.getAbsolutePath());
                String out = file.getName();
                String[] outs = out.split("\\_");
                if (outs.length >=2) {
                    out = ". " + outs[outs.length-2] + "_" + outs[outs.length-1];
                }
                else {
                    out = ". " + out;
                }
                CLIIOHelpers.info(fileNames.size() + out);

            }
            else {
                if (fileName.endsWith("json")) continue;
                if (fileName.contains("incomplete")) continue;
                File file = new File(backupDir, fileName);
                fileNames.add(file.getAbsolutePath());
                String out = file.getName();
                out = out.replaceAll(".zip", "");
                //out = out.replaceAll(worldPath + "_", ": ");
                out = out.replaceAll("backup_", ": ");
                out = out.replaceAll("-partial", "\u001B[33m partial\u001B[0m");
                out = out.replaceAll("-full", "\u001B[32m full\u001B[0m");
                CLIIOHelpers.info(fileNames.size() + out);

            }
        }

        try {
            String line = CLIIOHelpers.input.nextLine();
            if (line == "") {
                CLIIOHelpers.warn("Please enter a number.");
                return getBackupDate(backupDir, exportMode);
            }
            inputType = Integer.parseInt(line);
        } catch (InputMismatchException | NumberFormatException e) {
            CLIIOHelpers.warn("That was not a number. Please enter a number.");
            return getBackupDate(backupDir, exportMode);
        }

        if (inputType < 1 || inputType > fileNames.size()) {
            CLIIOHelpers.warn("Please enter a number between " + fileNames.size() + ".");
            return getBackupDate(backupDir, exportMode);
        }
        
        return inputType - 1;
    }