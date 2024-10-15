    void registerWikiSyntaxName(TestUtils testUtils) throws Exception
    {
        AbstractRegistrationPage registrationPage = setUp(testUtils, false, false);
        String password = "SomePassword";
        String firstName = "]]{{/html}}{{html clean=false}}HT&amp;ML";
        String lastName = "]]{{/html}}";
        String username = "WikiSyntaxName";
        registrationPage.fillRegisterForm(firstName, lastName, username, password, password, "wiki@example.com");
        assertTrue(validateAndRegister(testUtils, false, false, registrationPage));

        assertEquals(String.format("%s %s (%s): Registration successful.", firstName, lastName, username),
            ((RegistrationPage) registrationPage).getRegistrationSuccessMessage().orElseThrow());
    }