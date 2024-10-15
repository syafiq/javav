	public WelcomeParser() throws ParserConfigurationException, SAXException, FactoryConfigurationError {
		@SuppressWarnings("restriction")
		SAXParser p = org.eclipse.core.internal.runtime.XmlProcessorFactory.createSAXParserWithErrorOnDOCTYPE(true);
		parser = p;
		parser.getXMLReader().setContentHandler(this);
		parser.getXMLReader().setDTDHandler(this);
		parser.getXMLReader().setEntityResolver(this);
		parser.getXMLReader().setErrorHandler(this);
	}