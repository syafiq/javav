    public static boolean validate(String name) {
        if (name == null || name.length() > 10_000) {
            return false;
        }
        Matcher m = PATTERN.matcher(name.toLowerCase());
        return m.find();
    }