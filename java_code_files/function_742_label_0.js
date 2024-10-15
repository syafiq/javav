	private static Document parse(InputStream in) throws SettingsImportExportException {
		DocumentBuilderFactory factory = XmlProcessorFactoryCdt.createDocumentBuilderFactoryWithErrorOnDOCTYPE();
		factory.setValidating(false);
		factory.setNamespaceAware(false);
		factory.setIgnoringComments(true);

		try {
			DocumentBuilder parser = factory.newDocumentBuilder();
			parser.setErrorHandler(ABORTING_ERROR_HANDER); // causes SAXException to be thrown on any parse error
			InputSource input = new InputSource(in); // TODO should I be using an InputSource?
			Document doc = parser.parse(input);
			return doc;

		} catch (Exception e) {
			throw new SettingsImportExportException(e);
		}
	}