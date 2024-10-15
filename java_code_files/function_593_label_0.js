	private static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory factory = XmlProcessorFactory.createDocumentBuilderFactoryWithErrorOnDOCTYPE();
		factory.setNamespaceAware(true);
		return factory.newDocumentBuilder();
	}