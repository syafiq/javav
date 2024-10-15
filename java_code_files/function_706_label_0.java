	public void initializeFromMemento(String data) {
		if (data == null || data.isBlank()) {
			// An empty string will cause a sax parser error below
			return;
		}

		Element root = null;
		DocumentBuilder parser;
		try {
			parser = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			parser.setErrorHandler(new DefaultHandler());
			root = parser.parse(new InputSource(new StringReader(data))).getDocumentElement();
			String value = root.getAttribute("file"); //$NON-NLS-1$
			if (value == null)
				throw new Exception();
			soundFile = new File(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}