    void setUp() throws Exception
    {
        this.xwiki.initializeMandatoryDocuments(this.context);

        this.loadPage(SEARCH_SUGGEST_SOURCE_CLASS);
        this.searchSuggestSourceSheetDocument = this.loadPage(SEARCH_SUGGEST_SOURCE_SHEET);
    }