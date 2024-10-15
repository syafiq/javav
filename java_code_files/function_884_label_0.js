    void setUp() throws Exception
    {
        // Mock the Query Service to return a job.
        this.queryService = this.oldcore.getMocker().registerMockComponent(ScriptService.class, "query",
            QueryManagerScriptService.class,
            true);
        when(this.queryService.xwql(anyString())).thenReturn(this.query);
        when(this.query.execute()).thenReturn(List.of("Scheduler.TestJob"));

        // Mock the Token Service to get a consistent CSRF token throughout the tests.
        this.tokenService = this.oldcore.getMocker().registerMockComponent(ScriptService.class, "csrf",
            CSRFTokenScriptService.class, true);
        when(this.tokenService.getToken()).thenReturn(CSRF_TOKEN);
        when(this.tokenService.isTokenValid(CSRF_TOKEN)).thenReturn(true);

        // Spy the Scheduler Plugin to obtain a mocked API.
        this.schedulerPluginApi = mock(SchedulerPluginApi.class);
        doReturn(this.schedulerPluginApi).when(this.oldcore.getSpyXWiki()).getPluginApi(eq("scheduler"),
            any(XWikiContext.class));

        this.xwiki.initializeMandatoryDocuments(this.context);

        // Create a new job and keep a reference to its API.
        XWikiDocument testJob = new XWikiDocument(new DocumentReference("xwiki", "Scheduler", "TestJob"));
        BaseObject testJobObject = testJob.newXObject(SchedulerJobClassDocumentInitializer.XWIKI_JOB_CLASSREFERENCE,
            this.context);
        this.xwiki.saveDocument(testJob, this.context);
        this.testJobObjectApi = new Object(testJobObject, this.context);

        // Fake programming access level to display the complete page.
        when(this.oldcore.getMockRightService().hasAccessLevel(eq("programming"), anyString(), anyString(),
            any(XWikiContext.class))).thenReturn(true);
    }