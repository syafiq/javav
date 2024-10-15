    public void setUniqueId(String uniqueId) {
        if (uniqueId.contains("..")) {
            throw new IllegalArgumentException("Invalid unique id");
        }
        this.uniqueId = uniqueId.trim();
    }