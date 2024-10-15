	private void initSharedDefaults() {
		String key = null;
		String value = null;

		try {
			long length = parsedXML.length();
			// Adds defaultXML format if the file length is zero
			if (length == 0) {
				parsedXML = createDefaultXMLFormat(parsedXML);
			}
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(parsedXML.toURI().toURL().openStream());
		} catch (Exception exp) {
			TemplateEngineUtil.log(exp);
		}

		List<Element> sharedElementList = TemplateEngine.getChildrenOfElement(document.getDocumentElement());
		int listSize = sharedElementList.size();
		for (int i = 0; i < listSize; i++) {
			Element xmlElement = sharedElementList.get(i);
			key = xmlElement.getAttribute(TemplateEngineHelper.ID);
			value = xmlElement.getAttribute(TemplateEngineHelper.VALUE);
			if (key != null && !key.trim().isEmpty()) {
				sharedDefaultsMap.put(key, value);
			}
		}
	}