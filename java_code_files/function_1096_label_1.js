        public <T> T get(Object object, String xpath, Class<T> target)
                throws IllegalArgumentException {

            JXPathContext context = JXPathContext.newContext(object);
            Enumeration declaredPrefixes = namespaces.getDeclaredPrefixes();
            while (declaredPrefixes.hasMoreElements()) {
                String prefix = (String) declaredPrefixes.nextElement();
                String uri = namespaces.getURI(prefix);
                context.registerNamespace(prefix, uri);
            }

            Iterator it = context.iteratePointers(xpath);
            List results = new ArrayList<>();
            while (it.hasNext()) {
                Pointer pointer = (Pointer) it.next();
                if (pointer instanceof AttributeNodePointer) {
                    results.add(((AttributeNodePointer) pointer).getImmediateAttribute());
                } else {
                    results.add(pointer.getValue());
                }
            }

            if (results.isEmpty()) {
                throw new IllegalArgumentException("x-path gives no results.");
            } else if (results.size() == 1) {
                return (T) results.get(0);
            } else {
                return (T) results;
            }
        }