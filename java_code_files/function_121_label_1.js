    private void verifyImports(TestInfo info)
    {
        String testName = info.getTestMethod().get().getName();
        // Test word file
        ViewPage resultPage = importFile(testName, "msoffice.97-2003/Test.doc");
        assertTrue(StringUtils.contains(resultPage.getContent(), "This is a test document."));
        deletePage(testName);

        // Test power point file
        resultPage = importFile(testName, "msoffice.97-2003/Test.ppt");
        AttachmentsPane attachmentsPane = new AttachmentsViewPage().openAttachmentsDocExtraPane();
        assertTrue(attachmentsPane.attachmentExistsByFileName("Test-slide0.jpg"));
        assertTrue(attachmentsPane.attachmentExistsByFileName("Test-slide1.jpg"));
        assertTrue(attachmentsPane.attachmentExistsByFileName("Test-slide2.jpg"));
        assertTrue(attachmentsPane.attachmentExistsByFileName("Test-slide3.jpg"));
        deletePage(testName);

        // Test excel file
        resultPage = importFile(testName, "msoffice.97-2003/Test.xls");
        assertTrue(StringUtils.contains(resultPage.getContent(), "Sheet1"));
        assertTrue(StringUtils.contains(resultPage.getContent(), "Sheet2"));
        deletePage(testName);

        // Test ODT file
        resultPage = importFile(testName, "ooffice.3.0/Test.odt");
        assertTrue(StringUtils.contains(resultPage.getContent(), "This is a test document."));
        WikiEditPage wikiEditPage = resultPage.editWiki();
        String regex = "(?<imageName>Test_html_[\\w]+\\.png)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(wikiEditPage.getContent());
        assertTrue(matcher.find());
        String imageName = matcher.group("imageName");
        resultPage = wikiEditPage.clickCancel();
        attachmentsPane = new AttachmentsViewPage().openAttachmentsDocExtraPane();
        assertEquals(4, attachmentsPane.getNumberOfAttachments());
        assertTrue(attachmentsPane.attachmentExistsByFileName(imageName));
        deletePage(testName);

        // Test ODP file
        resultPage = importFile(testName, "ooffice.3.0/Test.odp");
        attachmentsPane = new AttachmentsViewPage().openAttachmentsDocExtraPane();
        assertTrue(attachmentsPane.attachmentExistsByFileName("Test-slide0.jpg"));
        assertTrue(attachmentsPane.attachmentExistsByFileName("Test-slide1.jpg"));
        assertTrue(attachmentsPane.attachmentExistsByFileName("Test-slide2.jpg"));
        assertTrue(attachmentsPane.attachmentExistsByFileName("Test-slide3.jpg"));
        deletePage(testName);

        // Test ODS file
        resultPage = importFile(testName, "ooffice.3.0/Test.ods");
        assertTrue(StringUtils.contains(resultPage.getContent(), "Sheet1"));
        assertTrue(StringUtils.contains(resultPage.getContent(), "Sheet2"));
        deletePage(testName);

        // Test ODT file with accents
        resultPage = importFile(testName, "ooffice.3.0/Test_accents & é$ù !-_.+();@=.odt");
        assertTrue(StringUtils.contains(resultPage.getContent(), "This is a test document."));
        wikiEditPage = resultPage.editWiki();
        regex = "(?<imageName>Test_accents & e\\$u !-_\\.\\+\\(\\);\\\\@=_html_[\\w]+\\.png)";
        pattern = Pattern.compile(regex, Pattern.MULTILINE);
        matcher = pattern.matcher(wikiEditPage.getContent());
        assertTrue(matcher.find());
        imageName = matcher.group("imageName");
        resultPage = wikiEditPage.clickCancel();
        attachmentsPane = new AttachmentsViewPage().openAttachmentsDocExtraPane();
        assertEquals(4, attachmentsPane.getNumberOfAttachments());
        // the \ before the @ needs to be removed as it's not in the filename
        assertTrue(attachmentsPane.attachmentExistsByFileName(imageName.replaceAll("\\\\@", "@")));
        deletePage(testName);

        // Test power point file with accents
        resultPage = importFile(testName, "msoffice.97-2003/Test_accents & é$ù !-_.+();@=.ppt");
        attachmentsPane = new AttachmentsViewPage().openAttachmentsDocExtraPane();
        assertTrue(attachmentsPane.attachmentExistsByFileName("Test_accents & e$u !-_.+();@=-slide0.jpg"));
        assertTrue(attachmentsPane.attachmentExistsByFileName("Test_accents & e$u !-_.+();@=-slide1.jpg"));
        assertTrue(attachmentsPane.attachmentExistsByFileName("Test_accents & e$u !-_.+();@=-slide2.jpg"));
        assertTrue(attachmentsPane.attachmentExistsByFileName("Test_accents & e$u !-_.+();@=-slide3.jpg"));
        wikiEditPage = resultPage.editWiki();
        assertTrue(wikiEditPage.getContent().contains("[[image:Test_accents & e$u !-_.+();\\@=-slide0.jpg]]"));
        resultPage = wikiEditPage.clickCancel();
        deletePage(testName);
    }