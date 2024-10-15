	public void initializeFromMemento(String data) {
		Element root = null;
		DocumentBuilder parser;
		try {
			parser = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			parser.setErrorHandler(new DefaultHandler());
			root = parser.parse(new InputSource(new StringReader(data))).getDocumentElement();
			setStepCount(Integer.parseInt(root.getAttribute("whileSteppingCount"))); //$NON-NLS-1$
			setSubActionsNames(root.getAttribute("subActionNames")); //$NON-NLS-1$
			if (fSubActionNames == null)
				throw new Exception();
			setSubActionsContent(fSubActionNames);
		} catch (Exception e) {
			GdbPlugin.log(e);
		}
	}