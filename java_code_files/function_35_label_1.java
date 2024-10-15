    public String render(String iconName)
    {
        try {
            return iconManager.render(iconName);
        } catch (IconException e) {
            setLastError(e);
            return null;
        }
    }