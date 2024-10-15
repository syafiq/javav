    protected boolean stream(ElementHandler handler) {
        // create an xpath context from the root element
        // TODO: cache the context, should work just the same
        //        JXPathIntrospector.registerDynamicClass(ElementHandlerImpl.class,
        //            ElementHandlerPropertyHandler.class);
        JXPathIntrospector.registerDynamicClass(NodeImpl.class, NodePropertyHandler.class);

        //        ElementHandler rootHandler =
        //        	((DocumentHandler) handlers.firstElement()).getDocumentElementHandler();
        Node root = handlers.firstElement().getParseNode();
        JXPathContext jxpContext = JXPathContextFactory.newInstance().newContext(null, root);

        jxpContext.setLenient(true);

        Iterator itr = jxpContext.iterate(xpath);

        while (itr.hasNext()) {
            Object obj = itr.next();

            if (handler.getParseNode().equals(obj)) {
                return true;
            }
        }

        return false;
    }