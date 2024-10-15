	public void initializeFromMemento(String data) {
		Element root = null;
		DocumentBuilder parser;
		try {
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			root = parser.parse(new InputSource(new StringReader(data))).getDocumentElement();
			fEvalString = root.getAttribute("evalString"); //$NON-NLS-1$
			if (fEvalString == null)
				throw new Exception();
		} catch (Exception e) {
			GdbPlugin.log(e);
		}
	}