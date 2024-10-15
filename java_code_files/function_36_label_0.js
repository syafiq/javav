    public String render(String iconName, String iconSetName)
    {
        return String.format("{{displayIcon name=\"%s\" iconSet=\"%s\"/}}", escapeXWiki21(iconName),
            escapeXWiki21(iconSetName));
    }