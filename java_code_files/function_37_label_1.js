    public String render(String iconName, String iconSetName)
    {
        try {
            return iconManager.render(iconName, iconSetName);
        } catch (IconException e) {
            setLastError(e);
            return null;
        }
    }