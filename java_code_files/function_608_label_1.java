	public void parseInputStream(InputStream is, boolean expandURLs) {

		documentBuilderFactory.setNamespaceAware(true);
		documentBuilderFactory.setIgnoringComments(true);

		reset();
		try {
			DocumentBuilder parser = documentBuilderFactory
					.newDocumentBuilder();
			parser.setErrorHandler(new ParseErrorHandler());
			InputSource source = new InputSource(is);
			Document doc = parser.parse(source);
			processDocument(doc, expandURLs);
		} catch (ParserConfigurationException | SAXException e) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, e, " " + e.getMessage()); //$NON-NLS-1$
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
	}