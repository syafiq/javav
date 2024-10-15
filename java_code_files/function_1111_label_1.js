    public static String serialiseBackupName(String in) {
        Date date = new Date();
        String pattern = "yyyy-MM-dd_HH-mm-ss";
        
        return in + "_" + new SimpleDateFormat(pattern).format(date);
    }