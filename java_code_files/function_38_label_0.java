    public String render(String iconName, String iconSetName, boolean fallback)
    {
        return String.format("{{displayIcon name=\"%s\" iconSet=\"%s\" fallback=\"%b\"/}}", escapeXWiki21(iconName),
            escapeXWiki21(iconSetName), fallback);
    }