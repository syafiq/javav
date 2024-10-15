    void simpleRESTPost(TestUtils setup, TestReference reference) throws Exception
    {
        setup.loginAsSuperAdmin();
        setup.deletePage(reference);

        String content = "{{html clean=\"false\"}}"
            + "<div id='results'></div>"
            + "<script>"
            + IOUtils.toString(
            Objects.requireNonNull(getClass().getResourceAsStream("/FormTokenInjectionIT/testCode.js")),
            StandardCharsets.UTF_8)
            + "</script>"
            + "{{/html}}";
        setup.createPage(reference, content);

        ViewPage viewPage = setup.gotoPage(reference);
        String pageContent = viewPage.getContent();

        assertAll(
            Stream.of(
                "Simple POST: 201",
                "Only Request: 201",
                "Request with init: 201",
                "Simple with array headers: 201",
                "Request with init body",
                "Request Body",
                "Simple with array headers body"
            ).map(expected -> (() -> assertThat(pageContent, containsString(expected))))
        );
    }