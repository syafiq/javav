        void shouldRespondWithMessageInHTMLWhenAcceptHeaderContainsHTML(String acceptHeaderValue) throws Exception {
            webAppIsStarting();

            MockResponse response = request("/go/pipelines", acceptHeaderValue);

            assertLoadingResponseInHTML(response);
        }