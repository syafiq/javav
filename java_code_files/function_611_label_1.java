	public WelcomeParser() throws ParserConfigurationException, SAXException,
			FactoryConfigurationError {
		super();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setFeature("http://xml.org/sax/features/namespaces", true); //$NON-NLS-1$
		parser = factory.newSAXParser();

		parser.getXMLReader().setContentHandler(this);
		parser.getXMLReader().setDTDHandler(this);
		parser.getXMLReader().setEntityResolver(this);
		parser.getXMLReader().setErrorHandler(this);
	}