    private static JXPathContext initialiseContext(NamespaceSupport ns, Document doc) {
        JXPathContext context = JXPathContext.newContext(doc);
        addNamespaces(ns, context);
        context.setLenient(true);
        return context;
    }