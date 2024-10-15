	public void initializeFromMemento(String data) {
		Element root = null;
		DocumentBuilder parser;
		try {
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			root = parser.parse(new InputSource(new StringReader(data))).getDocumentElement();
			String value = root.getAttribute("configName"); //$NON-NLS-1$
			if (value == null)
				throw new Exception();
			externalToolName = value;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}