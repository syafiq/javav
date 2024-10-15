    private void assertLoadingResponseInHTML(MockResponse response, String expectedBody) {
        assertTrue(response.
                hasStatus(503).
                withContentType("text/html").
                withBody(expectedBody).
                withNoCaching().
                done());
    }