    protected Schema getEmbeddedLwM2mSchema() throws SAXException {
        InputStream inputStream = DDFFileValidator.class.getResourceAsStream(schema);
        Source source = new StreamSource(inputStream);
        SchemaFactory schemaFactory = createSchemaFactory();
        return schemaFactory.newSchema(source);
    }