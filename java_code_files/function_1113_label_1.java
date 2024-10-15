    private static void restorePartialZip(int index, File worldFile) {

        HashMap<String, Object> filePaths = new HashMap<>();
        HashMap<String, String> dates = new HashMap<>();
        HashMap<String, ZipFile> entryOwners = new HashMap<>();
        try {
            File backup = new File(fileNames.get(index));
    
            addBackupNamesToLists(backup, entryOwners, filePaths, dates, "\u001B[32m");

            ZipEntry select = ((ZipEntry) CLIIOHelpers.getFileToRestore(filePaths, "", worldFile));

            File outputFile = new File(worldFile, select.toString());
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            CLIIOHelpers.info("Restoring " + select.toString() + "...");

            byte[] buffer = new byte[1028];
            InputStream inputSteam = entryOwners.get(select.toString()).getInputStream(select);
            int length;
            while ((length = inputSteam.read(buffer, 0, buffer.length)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {

        }



         
    }