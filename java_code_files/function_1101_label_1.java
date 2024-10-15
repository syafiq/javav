    public static String getSingleXPathValue(
            NamespaceSupport ns, String xpathString, Document doc) {
        String id = null;
        JXPathContext context = initialiseContext(ns, doc);
        try {
            Object ob = context.getValue(xpathString);
            id = (String) ob;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error reading xpath " + xpathString, e);
        }
        return id;
    }