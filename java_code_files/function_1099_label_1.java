    private static void addNamespaces(NamespaceSupport ns, JXPathContext context) {
        @SuppressWarnings("unchecked")
        Enumeration<String> prefixes = ns.getPrefixes();
        while (prefixes.hasMoreElements()) {
            String prefix = prefixes.nextElement();
            String uri = ns.getURI(prefix);
            context.registerNamespace(prefix, uri);
        }
    }