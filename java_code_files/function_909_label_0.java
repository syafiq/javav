    void checkRSSFeedContent() throws Exception
    {
        String unescapedText = "<b>}}}{{noscript}}</b>";
        String escapedText = "&lt;b&gt;}}}{{noscript}}&lt;/b&gt;";

        this.request.put("text", unescapedText);
        this.context.setAction("get");

        XWikiDocument databaseSearchDocument = loadPage(DATABASE_SEARCH_REFERENCE);
        this.context.setDoc(databaseSearchDocument);

        // Get directly the writer to check the RSS feed.
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        this.response = new XWikiServletResponseStub() {
            @Override
            public PrintWriter getWriter()
            {
                return writer;
            }
        };
        this.context.setResponse(this.response);

        String rssFeed = databaseSearchDocument.displayDocument(Syntax.PLAIN_1_0, this.context);
        assertTrue(rssFeed.isEmpty());

        rssFeed = out.toString();
        assertTrue(rssFeed.contains("<title>search.rss [" + escapedText + "]</title>"));
        assertTrue(rssFeed.contains("<description>search.rss [" + escapedText + "]</description>"));
    }