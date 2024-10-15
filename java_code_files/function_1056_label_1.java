        void shouldRespondWithSimpleMessageIfLoadingHTMLFileCannotBeLoaded() throws Exception {
            webAppIsStarting();
            loadingPageIsSetTo("/some-non-existent-file");

            MockResponse response = request("/go/pipelines", "text/html");

            assertLoadingResponseInHTML(response, "<h2>GoCD is starting up. Please wait ....</h2>");
        }