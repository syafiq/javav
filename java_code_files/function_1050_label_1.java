        void shouldRespondWithMessageInHTMLWhenAcceptHeaderIsMissing() throws Exception {
            webAppIsStarting();
            loadingPageIsSetTo("/test.loading.page.html");

            MockResponse response = request("/go/pipelines", null);

            assertLoadingResponseInHTML(response, "<div><b>GoCD server is starting. This comes from test.loading.page.html</b></div>");
        }