        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalAttributeException {

            if (object instanceof FeatureType) {
                throw new IllegalAttributeException(null, "feature type is immutable");
            }

            JXPathContext context =
                    JXPathUtils.newSafeContext(object, false, this.namespaces, true);
            context.setValue(xpath, value);

            assert value == context.getValue(xpath);
        }