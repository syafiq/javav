    void verifyScheduler(TestUtils setup)
    {
        setup.loginAsSuperAdmin();

        // Make sure the job doesn't exist. Note that we don't delete the job after the test is executed (@After)
        // because we want to remain on the same page in case of a test failure so that our TestDebugger rule can
        // collect accurate information about the failure. It's not a problem if the job remains scheduled because it
        // does nothing. Other tests should not rely on the number of scheduler jobs though.
        setup.deletePage("Scheduler", "Scheduler]]TestJob");

        // Create Job
        SchedulerHomePage schedulerHomePage = SchedulerHomePage.gotoPage();
        schedulerHomePage.setJobName("Scheduler]]TestJob");
        SchedulerEditPage schedulerEdit = schedulerHomePage.clickAdd();

        String jobName = "Tester problem";
        schedulerEdit.setJobName(jobName);
        schedulerEdit.setJobDescription(jobName);
        schedulerEdit.setCron("0 15 10 ? * MON-FRI");
        SchedulerPage schedulerPage = schedulerEdit.clickSaveAndView();
        schedulerHomePage = schedulerPage.backToHome();

        // View Job
        schedulerPage = schedulerHomePage.clickJobActionView(jobName);

        // Tests that a scheduler job page's default edit mode is Form
        // Note: This line below will fail if the page is not edited in Form mode!
        schedulerPage.edit();
        new SchedulerEditPage().setJobDescription("test");
        schedulerEdit.clickCancel();
        schedulerHomePage = schedulerPage.backToHome();

        // Edit Job
        schedulerEdit = schedulerHomePage.clickJobActionEdit(jobName);
        schedulerEdit.setJobDescription("Tester problem2");
        schedulerEdit.setCron("0 0/5 14 * * ?");
        schedulerPage = schedulerEdit.clickSaveAndView();
        schedulerHomePage = schedulerPage.backToHome();

        // Delete and Restore Job
        schedulerHomePage.clickJobActionDelete(jobName).clickYes();
        schedulerHomePage = SchedulerHomePage.gotoPage();
        assertFalse(setup.getDriver().hasElementWithoutWaiting(By.linkText(jobName)));
        // Note: since the page doesn't exist, we need to disable the space redirect feature so that we end up on the
        // terminal page that was removed.
        setup.gotoPage("Scheduler", "Scheduler]]TestJob", "view", "spaceRedirect=false");
        setup.getDriver().findElement(By.linkText("Restore")).click();
        schedulerPage = new SchedulerPage();
        schedulerPage.backToHome();

        // Schedule Job
        schedulerHomePage.clickJobActionSchedule(jobName);
        if (schedulerHomePage.hasError()) {
            fail("Failed to schedule job. Error [" + schedulerHomePage.getErrorMessage() + "]");
        }

        // Trigger Job (a Job can only be triggered after it's been scheduled)
        schedulerHomePage.clickJobActionTrigger(jobName);
        if (schedulerHomePage.hasError()) {
            fail("Failed to trigger job. Error [" + schedulerHomePage.getErrorMessage() + "]");
        }

        // Pause Job
        schedulerHomePage.clickJobActionPause(jobName);
        if (schedulerHomePage.hasError()) {
            fail("Failed to pause job. Error [" + schedulerHomePage.getErrorMessage() + "]");
        }

        // Resume Job
        schedulerHomePage.clickJobActionResume(jobName);
        if (schedulerHomePage.hasError()) {
            fail("Failed to resume job. Error [" + schedulerHomePage.getErrorMessage() + "]");
        }

        // Unschedule Job
        schedulerHomePage.clickJobActionUnschedule(jobName);
        if (schedulerHomePage.hasError()) {
            fail("Failed to unschedule job.  Error [" + schedulerHomePage.getErrorMessage() + "]");
        }
    }