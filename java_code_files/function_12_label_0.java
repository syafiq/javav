    void formTags(String tagName)
    {
        assertFalse(this.secureHTMLElementSanitizer.isElementAllowed(tagName));
    }