    void oldRevisionsAreRestricted(TestUtils utils, TestReference testReference) throws Exception
    {
        utils.loginAsSuperAdmin();

        utils.rest().delete(testReference);

        // Create first version of the page
        ViewPage vp = utils.createPage(testReference, "{{velocity}}" + CONTENT1 + "{{/velocity}}", TITLE);
        assertEquals(CONTENT1, vp.getContent());

        // Adds second version
        WikiEditPage wikiEditPage = vp.editWiki();
        wikiEditPage.setContent("{{velocity}}" + CONTENT2 + "{{velocity}}");
        vp = wikiEditPage.clickSaveAndView();

        assertEquals(CONTENT2, vp.getContent());

        // TODO: Remove when XWIKI-6688 (Possible race condition when clicking on a tab at the bottom of a page in
        // view mode) is fixed.
        vp.waitForDocExtraPaneActive("comments");

        HistoryPane historyTab = vp.openHistoryDocExtraPane();
        vp = historyTab.viewVersion("1.1");

        // In the preview the Velocity macro should be forbidden.
        assertThat(vp.getContent(), startsWith("Failed to execute the [velocity] macro."));

        // TODO: Remove when XWIKI-6688 (Possible race condition when clicking on a tab at the bottom of a page in
        // view mode) is fixed.
        vp.waitForDocExtraPaneActive("comments");

        historyTab = vp.openHistoryDocExtraPane();
        vp = historyTab.rollbackToVersion("1.1");

        // Rollback doesn't wait...
        // Wait for the comment tab to be selected since we're currently on the history tab and rolling
        // back is going to load a new page and make the focus active on the comments tab.
        vp.waitForDocExtraPaneActive("comments");

        // Assert that scripts are executed again after restoring the version.
        assertEquals(CONTENT1, vp.getContent());
    }