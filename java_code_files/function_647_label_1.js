	private String getValueFromBackEndStorate(String key) throws Exception {
		File parsedXML = TemplateEngineHelper.getSharedDefaultLocation("shareddefaults.xml");

		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(parsedXML.toURI().toURL().openStream());

		List<Element> sharedElementList = TemplateEngine.getChildrenOfElement(document.getDocumentElement());
		int listSize = sharedElementList.size();
		for (int i = 0; i < listSize; i++) {
			Element xmlElement = sharedElementList.get(i);
			String key2 = xmlElement.getAttribute(TemplateEngineHelper.ID);
			String value2 = xmlElement.getAttribute(TemplateEngineHelper.VALUE);
			if (key.equals(key2)) {
				return value2;
			}
		}

		return null;
	}