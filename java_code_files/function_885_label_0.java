    void checkInvalidCSRFToken() throws Exception
    {
        String wrongToken = "wrong token";

        this.request.put("do", "trigger");
        this.request.put("which", "Scheduler.TestJob");
        this.request.put("form_token", wrongToken);
        Document result = renderHTMLPage(SCHEDULER_WEB_HOME);

        verify(this.schedulerPluginApi, never()).triggerJob(any(Object.class));
        verify(this.tokenService).isTokenValid(wrongToken);
        assertEquals("xe.scheduler.invalidToken", result.getElementsByClass("errormessage").text());
    }