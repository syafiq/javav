    void verifySpaceFaucetEscaping(TestUtils setup, TestConfiguration testConfiguration) throws Exception {
        setup.loginAsSuperAdmin();

        String testDocumentLocation = "{{/html}}";
        setup.createPage(testDocumentLocation, "WebHome", "Test Document", testDocumentLocation);

        new SolrTestUtils(setup, computedHostURL(testConfiguration)).waitEmptyQueue();

        SolrSearchPage searchPage = SolrSearchPage.gotoPage();
        searchPage.search("\"Test Document\"");
        searchPage.toggleSpaceFaucet();
        assertEquals(testDocumentLocation + "\n1", searchPage.getSpaceFaucetContent());
    }