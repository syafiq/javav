    private URL getAbsoluteURI(String relativeURL) throws MalformedURLException
    {
        XWikiContext xcontext = this.xcontextProvider.get();
        URL baseURL = xcontext.getURLFactory().getServerURL(xcontext);
        return new URL(baseURL, relativeURL);
    }