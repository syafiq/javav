    private String getCacheKey(URL url)
    {
        String userPart = this.userReferenceSerializer.serialize(CurrentUserReference.INSTANCE);
        // Prepend the length of the user part to avoid any kind of confusion between user and URL.
        return String.format("%d:%s:%s", userPart.length(), userPart, url.toString());
    }