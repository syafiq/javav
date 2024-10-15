    protected Schema getEmbeddedLwM2mSchema() throws SAXException {
        InputStream inputStream = DDFFileValidator.class.getResourceAsStream(schema);
        Source source = new StreamSource(inputStream);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return schemaFactory.newSchema(source);
    }