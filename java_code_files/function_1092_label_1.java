            new PropertyAccessor() {

                @Override
                public boolean canHandle(Object object, String xpath, Class target) {
                    return object instanceof Map;
                }

                @Override
                @SuppressWarnings("unchecked")
                public <T> T get(Object object, String xpath, Class<T> target)
                        throws IllegalArgumentException {
                    JXPathContext context = JXPathContext.newContext(object);
                    context.setLenient(true);
                    return (T) context.getValue(xpath);
                }

                @Override
                public void set(Object object, String xpath, Object value, Class target)
                        throws IllegalAttributeException, IllegalArgumentException {
                    throw new IllegalAttributeException("not implemented");
                }
            };