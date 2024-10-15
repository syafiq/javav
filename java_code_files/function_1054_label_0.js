        void shouldRespondWithMessageInHTMLWhenRequestAcceptHeaderIsHTML(String target) throws Exception {
            webAppIsStarting();

            MockResponse response = request(target, "text/html");

            assertLoadingResponseInHTML(response);
        }