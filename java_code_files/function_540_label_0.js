    void compareRenderedImageChanges(TestUtils setup, TestReference testReference) throws Exception
    {
        setup.loginAsSuperAdmin();
        setup.attachFile(testReference, ATTACHMENT_NAME_1,
            getClass().getResourceAsStream("/AttachmentIT/image.gif"), false);
        // Upload the image a second time under a different name to check that the content and not the URL is used
        // for comparison when changing the URL to the second image.
        setup.attachFile(testReference, ATTACHMENT_NAME_2,
            getClass().getResourceAsStream("/AttachmentIT/image.gif"), false);
        String url1 = getLocalAttachmentURL(setup, testReference, ATTACHMENT_NAME_1);
        ViewPage viewPage = setup.createPage(testReference, String.format(IMAGE_SYNTAX, url1));
        String firstRevision = viewPage.getMetaDataValue("version");
        // Create a second revision with the new image.
        String url2 = getLocalAttachmentURL(setup, testReference, ATTACHMENT_NAME_2);
        viewPage = setup.createPage(testReference, String.format(IMAGE_SYNTAX, url2));
        String secondRevision = viewPage.getMetaDataValue("version");

        // Open the history pane.
        ComparePage compare = viewPage.openHistoryDocExtraPane().compare(firstRevision, secondRevision);
        RenderedChanges renderedChanges = compare.getChangesPane().getRenderedChanges();
        assertTrue(renderedChanges.hasNoChanges());

        // Upload a new image with different content to verify that the changes are detected.
        setup.attachFile(testReference, ATTACHMENT_NAME_3,
            getClass().getResourceAsStream("/AttachmentIT/SmallSizeAttachment.png"), false);

        // Create a third revision with the new image.
        String url3 = getLocalAttachmentURL(setup, testReference, ATTACHMENT_NAME_3);
        viewPage = setup.createPage(testReference, String.format(IMAGE_SYNTAX, url3));
        String thirdRevision = viewPage.getMetaDataValue("version");

        // Open the history pane.
        compare = viewPage.openHistoryDocExtraPane().compare(secondRevision, thirdRevision);
        renderedChanges = compare.getChangesPane().getRenderedChanges();
        assertFalse(renderedChanges.hasNoChanges());
        List<WebElement> changes = renderedChanges.getChangedBlocks();
        assertEquals(2, changes.size());

        // Check that the first change is the deletion and the second change the insertion of the new image.
        WebElement firstChange = changes.get(0);
        WebElement secondChange = changes.get(1);
        assertEquals("deleted", firstChange.getAttribute("data-xwiki-html-diff-block"));
        assertEquals("inserted", secondChange.getAttribute("data-xwiki-html-diff-block"));
        WebElement deletedImage = firstChange.findElement(By.tagName("img"));
        WebElement insertedImage = secondChange.findElement(By.tagName("img"));

        // Check that the src attribute of the deleted image ends with the image2 (don't check the start as it
        // depends on the container setup and the nested/non-nested test execution).
        assertEquals(url2, deletedImage.getAttribute("src"));

        // Compute the expected base64-encoded content of the inserted image. The HTML diff embeds both images but
        // replaces the deleted image by the original URL again after the diff computation.
        String expectedInsertedImageContent = Base64.getEncoder().encodeToString(
            IOUtils.toByteArray(getClass().getResourceAsStream("/AttachmentIT/SmallSizeAttachment.png")));
        assertEquals("data:image/png;base64," + expectedInsertedImageContent, insertedImage.getAttribute("src"));
    }