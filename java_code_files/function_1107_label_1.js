    public static void main(String args[]){
        
        

        if (System.console() != null) {
            AnsiConsole.systemInstall(); //this gets ansi escape codes working on windows. this was a FUCKING PAIN IN MY ASS
        }
        
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
         
        CLIIOHelpers.info("Advanced Backups - Version " + AdvancedBackupsCLI.class.getPackage().getImplementationVersion());
        CLIIOHelpers.info("Note : this cannot restore backups made prior to the 3.0 release.");
        CLIIOHelpers.info("Searching for properties...", false);

        
        Properties props = new Properties();
        File file = new File(serverDir, "config/AdvancedBackups.properties");
        FileReader reader;
        try {
            reader = new FileReader(file);   
            props.load(reader);

            backupLocation = props.getProperty("config.advancedbackups.path");
            type = props.getProperty("config.advancedbackups.type");
        } catch (Exception e) {
            CLIIOHelpers.error("ERROR LOADING PROPERTIES!");
            CLIIOHelpers.error(getStackTrace(e));
            CLIIOHelpers.error("");
            CLIIOHelpers.error("");
            CLIIOHelpers.error("Ensure you're running this from within the mods directory, and the config file is in the parent directory!");
            // Fatal, cannot proceed
            return;
        }

        if (backupLocation == null || type == null) {
            CLIIOHelpers.error("ERROR LOADING PROPERTIES!");
            CLIIOHelpers.error("Backup location : " + backupLocation);
            CLIIOHelpers.error("Type : " + type);
            // Fatal, cannot proceed
            return;
        }

        CLIIOHelpers.info("Config loaded!");

        //What's a good way to check between absolute and relative? Not all absolute paths will start with /..
        File backupDir = new File(serverDir, backupLocation.replaceAll(Pattern.quote("." + File.separator), ""));

        if (!backupDir.exists()) {
            //Is it absolute?
            backupDir = new File(backupLocation.replaceAll(Pattern.quote("." + File.separator), ""));
            if (!backupDir.exists()) {
                CLIIOHelpers.error("Could not find backup directory!");
                CLIIOHelpers.error(backupDir.getAbsolutePath());
                CLIIOHelpers.error("Have you made any backups before?");
                //Fatal, cannot continue
                return;
            }
        }

        
        //check for backups from "other mods"
        boolean flag = false;
        ArrayList<File> otherBackups = new ArrayList<>();
        for (File b : backupDir.listFiles()) {
            if (b.getName().endsWith("zip")) {
                flag = true;
                otherBackups.add(b);
            }

        }

        if (flag) {
            String result = CLIIOHelpers.getSelectionFromList("Backups from another mod have been found. These can be restored if you want.\nWould you want to work with these backups?", 
            Arrays.asList(new String[]{"Use backups from AdvancedBackups", "Use backups from other mod"}));
            if (result == "Use backups from other mod") {
                restoreOtherModZip(backupDir);
                return;
            }
        }

        type = CLIIOHelpers.getBackupType(type);
        if (type.equals("snapshot (command-made only)")) type = "snapshots";

        /*/
        if (backupLocation.startsWith(Pattern.quote(File.separator)) || backupLocation.indexOf(":") == 1) {
            backupDir = new File(backupLocation, File.separator + type + File.separator);
        }
        else {
            backupDir = new File(serverDir, backupLocation.replaceAll(Pattern.quote("." + File.separator), "") + File.separator + type + File.separator);
        }
*/
        
               
        boolean exportMode = false;
        int backupDateIndex;

        

        CLIIOHelpers.info("Do you want to export a backup, restore the entire world state at this point, or a singular file?");

        String restore = CLIIOHelpers.getSelectionFromList("Enter a number.",
            Arrays.asList(new String[]{"Export backup as zip", "Restore single file", "Restore entire world"}));
        
            
        if (restore.equals("Export backup as zip")) {
            exportMode = true;
        }
        
        
        worldFile = CLIIOHelpers.getWorldFile(serverDir);
        worldPath = worldFile.getName().replace(" ", "_");

        backupDir = new File(backupDir, worldFile.getName() + "/" + type);

        try {
            backupDateIndex = getBackupDate(backupDir, exportMode);
        } catch (IOException e) {
            CLIIOHelpers.error("ERROR VIEWING BACKUPS!");
            e.printStackTrace();
            return;
        }

        if (exportMode) { 
            worldFile = new File(serverDir, "AdvancedBackups.temp");
            worldFile.mkdirs();
        }


        if (!CLIIOHelpers.confirmWarningMessage()) {
            CLIIOHelpers.error("ABORTED - WILL NOT PROCEED.");
            return;
        }

        CLIIOHelpers.info("Preparing...");
        


        switch(restore) {
            case "Restore entire world" : {
                //No going back now!
                CLIIOHelpers.info("Backing up current world state...");
                CLIIOHelpers.info("Backup saved to : " + deleteEntireWorld(worldFile, false));
                switch(type) {
                    case "snapshots" :
                    case "zips" : { 
                        restoreFullZip(backupDateIndex, worldFile);
                        return;
                    }
                    case "differential" : {
                        restoreFullDifferential(backupDateIndex, worldFile);
                        return;
                    }
                    case "incremental" : {
                        restoreFullIncremental(backupDateIndex, worldFile);
                        return;
                    }
                }
            }
            case "Restore single file" : {
                switch(type) {
                    case "snapshots" :
                    case "zips" : {
                        restorePartialZip(backupDateIndex, worldFile);
                        return;
                    }
                    case "differential" : {
                        restorePartialDifferential(backupDateIndex, worldFile);
                        return;
                    }
                    case "incremental" : {
                        restorePartialIncremental(backupDateIndex, worldFile);
                        return;
                    }
                }
            }
            case "Export backup as zip" : {

                CLIIOHelpers.info("Restoring to temporary directory...");

                switch(type) {
                    case "snapshots" :
                    case "zips" : { 
                        restoreFullZip(backupDateIndex, worldFile);
                        break;
                    }
                    case "differential" : {
                        restoreFullDifferential(backupDateIndex, worldFile);
                        break;
                    }
                    case "incremental" : {
                        restoreFullIncremental(backupDateIndex, worldFile);
                        break;
                    }
                }

                CLIIOHelpers.info("Done. Preparing to write to zip...");
                CLIIOHelpers.info("Export saved to : " + deleteEntireWorld(worldFile, true));
            }

        }
    }