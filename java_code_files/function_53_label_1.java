    public String render(String iconName, IconSet iconSet, String renderer) throws IconException
    {
        // The method should return an empty string in case no renderer or icon set was given
        if (renderer == null || iconSet == null) {
            return "";
        }
        // Get the icon
        Icon icon = iconSet.getIcon(iconName);

        // The icon may not exist
        if (icon == null) {
            // return an empty string. Idea: fallback on a different icon instead?
            return "";
        }

        // Add the icon set resources
        use(iconSet);

        // Interpret the velocity command
        StringWriter contentToParse = new StringWriter();
        contentToParse.write("#set($icon = \"");
        contentToParse.write(icon.getValue());
        contentToParse.write("\")\n");
        contentToParse.write(renderer);

        return velocityRenderer.render(contentToParse.toString());
    }