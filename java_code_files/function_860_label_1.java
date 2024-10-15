    void getParametersWithCommentAloneOnLine() throws Exception
    {
        when(this.modelContext.getCurrentEntityReference()).thenReturn(new WikiReference("xwiki"));

        String paramsStr = "# a = 1\n"
            + "x=1\n"
            + "y=2\n"
            + "# ...\n"
            + "z=3";
        WikiUIExtensionParameters parameters =
            new WikiUIExtensionParameters("id", paramsStr, this.componentManager);
        parameters.get();

        verify(this.velocityEngine).evaluate(any(), any(), eq("id:x"), eq("1"));
        verify(this.velocityEngine).evaluate(any(), any(), eq("id:y"), eq("2"));
        verify(this.velocityEngine).evaluate(any(), any(), eq("id:z"), eq("3"));
    }