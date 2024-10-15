    protected SchemaFactory createSchemaFactory() {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            // Create Safe SchemaFactory (not vulnerable to XXE Attacks)
            // --------------------------------------------------------
            // There is several recommendation from different source we try to apply all, even if some are maybe
            // redundant.

            // from :
            // https://semgrep.dev/docs/cheat-sheets/java-xxe/
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // from :
            // https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html#schemafactory
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            throw new IllegalStateException("Unable to create SchemaFactory", e);
        }
        return factory;
    }