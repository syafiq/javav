	public void initializeFromMemento(String data) {
		Element root = null;
		DocumentBuilder parser;
		try {
			parser = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			parser.setErrorHandler(new DefaultHandler());
			root = parser.parse(new InputSource(new StringReader(data))).getDocumentElement();
			String value = root.getAttribute("message"); //$NON-NLS-1$
			if (value == null)
				throw new Exception();
			message = value;
			value = root.getAttribute("evalExpr"); //$NON-NLS-1$
			if (value == null)
				throw new Exception();
			evaluateExpression = Boolean.valueOf(value).booleanValue();
			value = root.getAttribute("resume"); //$NON-NLS-1$
			if (value == null)
				throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}