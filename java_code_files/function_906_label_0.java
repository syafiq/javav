    void checkTranslationWithoutExpectedRights() throws Exception
    {
        Translation frTranslation = this.localization.getTranslation("xwiki.translation", Locale.FRENCH);
        assertEquals(
            "Failed to load and register the translation for locale [fr] from document [xwiki:space.Translations]. "
                + "Falling back to default locale.",
            this.logCapture.getMessage(0));
        assertEquals("root", frTranslation.getRawSource());
        verify(this.oldcore.getMockAuthorizationManager()).checkAccess(Right.ADMIN, null,
            TRANSLATION_ROOT_REFERENCE.getWikiReference());
    }