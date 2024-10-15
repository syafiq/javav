    public static List<String> getXPathValues(
            NamespaceSupport ns, String xpathString, Document doc) {
        JXPathContext context = initialiseContext(ns, doc);
        return getXPathValues(xpathString, context);
    }