    public static String getSafeInputFilenameFromExtension(String filename)
    {
        String extension = FileNameUtils.getExtension(filename);
        if (!SAFE_EXTENSION.matcher(extension).matches()) {
            extension = "";
        }

        return StringUtils.isBlank(extension) ? INPUT : INPUT + "." + extension;
    }