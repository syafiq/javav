	private Document getCachedDocument(final IPath path, final InputStream input) throws SAXException, IOException, ParserConfigurationException {
		if (path.equals(fCachedPath) && fCachedDocument != null)
			return fCachedDocument;
		DocumentBuilder parser= XmlProcessorFactoryLtk.createDocumentBuilderFactoryWithErrorOnDOCTYPE().newDocumentBuilder();
		parser.setErrorHandler(new DefaultHandler());
		final Document document= parser.parse(new InputSource(input));
		fCachedDocument= document;
		fCachedPath= path;
		return document;
	}