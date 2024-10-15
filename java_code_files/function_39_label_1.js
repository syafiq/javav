    public String render(String iconName, String iconSetName, boolean fallback)
    {
        try {
            return iconManager.render(iconName, iconSetName, fallback);
        } catch (IconException e) {
            setLastError(e);
            return null;
        }
    }