    public PropertyAccessor createPropertyAccessor(
            Class type, String xpath, Class target, Hints hints) {

        if (SimpleFeature.class.isAssignableFrom(type)) {
            /*
             * This class is not intended for use with SimpleFeature and causes problems when
             * discovered via SPI and used by code expecting SimpleFeature behaviour. In particular
             * WMS styling code may fail when this class is present. See GEOS-3525.
             */
            return null;
        }

        if (xpath == null) return null;

        if (!ComplexAttribute.class.isAssignableFrom(type)
                && !ComplexType.class.isAssignableFrom(type)
                && !AttributeDescriptor.class.isAssignableFrom(type)) return null;
        if (DEFAULT_GEOMETRY_NAME.equals(xpath)) return DEFAULT_GEOMETRY_ACCESS;

        // check for fid access
        if (FID_PATTERN.matcher(xpath).matches()) return FID_ACCESS;

        // check for simple property access
        // if (xpath.matches("(\\w+:)?(\\w+)")) {
        NamespaceSupport namespaces = null;
        if (hints != null) {
            namespaces =
                    (NamespaceSupport) hints.get(FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT);
        }
        if (namespaces == null) {
            return ATTRIBUTE_ACCESS;
        } else {
            return new FeaturePropertyAccessor(namespaces);
        }
        // }

        // return null;
    }