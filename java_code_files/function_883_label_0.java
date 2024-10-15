    void checkEscapingInJobNames(JobState status, String action) throws Exception
    {
        // Use the `noscript` macro to make sure that no code injection occurs.
        String jobName = "\">]]{{/html}}{{noscript /}}";
        String escapedJobName = "%22%3E%5D%5D%7B%7B%2Fhtml%7D%7D%7B%7Bnoscript%20%2F%7D%7D";

        // Create a new job with a name that needs escaping and get a reference to its API.
        XWikiDocument escapedJob = new XWikiDocument(new DocumentReference("xwiki", "Scheduler", jobName));
        BaseObject escapedJobObject =
            escapedJob.newXObject(SchedulerJobClassDocumentInitializer.XWIKI_JOB_CLASSREFERENCE, this.context);
        Object escapedJobObjectApi = new Object(escapedJobObject, this.context);
        this.xwiki.saveDocument(escapedJob, this.context);

        // Return the name of the new job through the Query Service.
        when(this.query.execute()).thenReturn(List.of("Scheduler." + jobName));

        // Set the status of the new job to control which action URLs will be rendered.
        when(this.schedulerPluginApi.getJobStatus(escapedJobObjectApi)).thenReturn(status);

        Document result = renderHTMLPage(SCHEDULER_WEB_HOME);
        Element actionLink = result.selectFirst(String.format("td a:contains(actions.%s)", action));
        assertNotNull(actionLink);

        // Check the proper escaping of the job name for the given action.
        assertEquals(String.format("path:/xwiki/bin/view/Scheduler/?do=%s&which=Scheduler"
            + ".%s&form_token=%s", action, escapedJobName, CSRF_TOKEN), actionLink.attr("href"));
    }