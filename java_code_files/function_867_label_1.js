    void getParametersWithCommentEndOfLine() throws Exception
    {
        when(this.modelContext.getCurrentEntityReference()).thenReturn(new WikiReference("xwiki"));

        String paramsStr = "x=1##b\n"
            + "y=2####x\n"
            + "z=3 ## xyz\n";
        WikiUIExtensionParameters parameters =
            new WikiUIExtensionParameters("id", paramsStr, this.componentManager);
        parameters.get();

        verify(this.velocityEngine).evaluate(any(), any(), eq("id:x"), eq("1##b"));
        verify(this.velocityEngine).evaluate(any(), any(), eq("id:y"), eq("2####x"));
        verify(this.velocityEngine).evaluate(any(), any(), eq("id:z"), eq("3 ## xyz"));
    }