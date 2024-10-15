    private static void restoreFullDifferential(int index, File worldFile) throws IllegalBackupException {
        //Do we need to check for past backups? if selected is a full backup, we do not.
        File backup = new File(fileNames.get(index));
        if (backup.getName().contains("-full")) {
            if (backup.isFile()) {
                restoreFullZip(index, worldFile);
                return;
            }
            restoreFolder(index, worldFile);
            return;
        }
        //find last FULL backup
        for (int i = index;i>=0;i--) {
            String name = fileNames.get(i);
            if (name.contains("-full")) {
                CLIIOHelpers.info("Restoring last full backup...");
                File file = new File(name);
                if (file.isFile()) {
                    restoreFullZip(i, worldFile);
                }
                else {
                    restoreFolder(i, worldFile);
                }
                break;
            }
        }
        CLIIOHelpers.info("\n\nRestoring selected backup...");
        if (backup.isFile()) {
            restoreFullZip(index, worldFile);
        }
        else {
            restoreFolder(index, worldFile);
        }
    }