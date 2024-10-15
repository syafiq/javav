    public String render(String iconName)
    {
        return String.format("{{displayIcon name=\"%s\"/}}", escapeXWiki21(iconName));
    }