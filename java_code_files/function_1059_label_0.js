        void shouldRespondWithMessageInHTMLWhenAcceptHeaderIsMissing() throws Exception {
            webAppIsStarting();

            MockResponse response = request("/go/pipelines", null);

            assertLoadingResponseInHTML(response);
        }