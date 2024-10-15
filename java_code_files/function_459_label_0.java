	public static SAXParser createSAXParserWithErrorOnDOCTYPE(boolean namespaceAware)
			throws ParserConfigurationException, SAXException {
		if (namespaceAware) {
			return SAX_FACTORY_ERROR_ON_DOCTYPE_NS.newSAXParser();
		}
		return SAX_FACTORY_ERROR_ON_DOCTYPE.newSAXParser();
	}