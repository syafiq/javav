    public static String normalizePath(String path) {
        char separator = File.separatorChar;

        if (separator != '/') {
            return path.replace(separator, '/');
        }

        return path;
    }