        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalAttributeException {

            if (object instanceof FeatureType) {
                throw new IllegalAttributeException(null, "feature type is immutable");
            }

            JXPathContext context = JXPathContext.newContext(object);
            Enumeration declaredPrefixes = namespaces.getDeclaredPrefixes();
            while (declaredPrefixes.hasMoreElements()) {
                String prefix = (String) declaredPrefixes.nextElement();
                String uri = namespaces.getURI(prefix);
                context.registerNamespace(prefix, uri);
            }
            context.setValue(xpath, value);

            assert value == context.getValue(xpath);
        }