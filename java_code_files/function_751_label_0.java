	public void initializeFromMemento(String data) {
		Element root = null;
		DocumentBuilder parser;
		try {
			parser = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			parser.setErrorHandler(new DefaultHandler());
			root = parser.parse(new InputSource(new StringReader(data))).getDocumentElement();
			String value = root.getAttribute("pauseTime"); //$NON-NLS-1$
			if (value == null)
				throw new Exception();
			pauseTime = Integer.parseInt(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}