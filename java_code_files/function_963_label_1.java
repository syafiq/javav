    public static String sanitizeUnknownFile(final File directory, final String entry) throws IOException, BrutException {
        if (entry.length() == 0) {
            throw new InvalidUnknownFileException("Invalid Unknown File");
        }

        if (new File(entry).isAbsolute()) {
            throw new RootUnknownFileException("Absolute Unknown Files is not allowed");
        }

        final String canonicalDirPath = directory.getCanonicalPath() + File.separator;
        final String canonicalEntryPath = new File(directory, entry).getCanonicalPath();

        if (!canonicalEntryPath.startsWith(canonicalDirPath)) {
            throw new TraversalUnknownFileException("Directory Traversal is not allowed");
        }

        // https://stackoverflow.com/q/2375903/455008
        return canonicalEntryPath.substring(canonicalDirPath.length());
    }