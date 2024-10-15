	public static Document newDocument() throws ParserConfigurationException {
		DocumentBuilder builder = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
		return builder.newDocument();
	}