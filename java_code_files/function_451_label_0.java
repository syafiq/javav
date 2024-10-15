    public static ConfigValue setComments(ConfigValue value, List<String> comments) {
        if (value.origin() instanceof SimpleConfigOrigin && value instanceof AbstractConfigValue) {
            return ((AbstractConfigValue) value).withOrigin(
                ((SimpleConfigOrigin) value.origin()).setComments(comments)
            );
        } else {
            return value;
        }
    }