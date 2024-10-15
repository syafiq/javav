    void checkValidCSRFToken() throws Exception
    {
        when(this.context.getAction()).thenReturn("get");
        this.request.put("text", "Hello");
        this.request.put("form_token", CSRF_TOKEN);
        Document result = renderHTMLPage(RTF_FRONTEND_CONVERT_HTML);

        verify(this.response, never()).setStatus(anyInt());
        verify(this.tokenService).isTokenValid(CSRF_TOKEN);
        assertEquals("$xwiki.getDocument('CKEditor.ContentSheet').getRenderedContent()",
            result.getElementsByTag("body").text());
    }