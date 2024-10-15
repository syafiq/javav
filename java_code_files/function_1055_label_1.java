        void shouldRespondWithMessageInHTMLWhenRequestAcceptHeaderIsHTML(String target) throws Exception {
            webAppIsStarting();
            loadingPageIsSetTo("/test.loading.page.html");

            MockResponse response = request(target, "text/html");

            assertLoadingResponseInHTML(response, "<div><b>GoCD server is starting. This comes from test.loading.page.html</b></div>");
        }