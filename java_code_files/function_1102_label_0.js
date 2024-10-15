    public static String getSingleXPathValue(
            NamespaceSupport ns, String xpathString, Document doc) {
        String id = null;
        try {
            Object ob = JXPathUtils.newSafeContext(doc, true, ns, false).getValue(xpathString);
            id = (String) ob;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error reading xpath " + xpathString, e);
        }
        return id;
    }