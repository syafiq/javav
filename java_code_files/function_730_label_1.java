	public void initializeFromMemento(String data) {
		Element root = null;
		DocumentBuilder parser;
		try {
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			root = parser.parse(new InputSource(new StringReader(data))).getDocumentElement();

			fCollectString = root.getAttribute(COLLECT_STRING_ATTR);
			if (fCollectString == null)
				fCollectString = ""; //$NON-NLS-1$

			String asStrings = root.getAttribute(COLLECT_AS_STRING_ATTR);
			if (asStrings != null) {
				fCharPtrAsStrings = Boolean.valueOf(asStrings);
			} else {
				fCharPtrAsStrings = false;
			}

			fCharPtrAsStringsLimit = null;
			String asStringsLimit = root.getAttribute(COLLECT_AS_STRING_LIMIT_ATTR);
			if (asStringsLimit != null) {
				try {
					fCharPtrAsStringsLimit = Integer.valueOf(asStringsLimit);
				} catch (NumberFormatException e) {
					// leave as null to disable
				}
			}
		} catch (Exception e) {
			GdbPlugin.log(e);
		}
	}