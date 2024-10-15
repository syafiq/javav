    private XDOMOfficeDocument createOfficeDocument(String content, String syntax) throws Exception
    {
        Parser parser = this.componentManager.getInstance(Parser.class, syntax);
        XDOM xdom = parser.parse(new StringReader(content));
        return new XDOMOfficeDocument(xdom, Collections.emptyMap(), this.componentManager, null);
    }