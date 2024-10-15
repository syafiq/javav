	public void updateToBackEndStorage(String updateName, String updateValue) {
		try {
			document = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE()
					.parse(parsedXML.toURI().toURL().openStream());
		} catch (Exception exp) {
			TemplateEngineUtil.log(exp);
		}

		persistDataMap.putAll(sharedDefaultsMap);
		List<Element> sharedElementList = TemplateEngine.getChildrenOfElement(document.getDocumentElement());
		int elementListSize = sharedElementList.size();

		for (int i = 0; i < elementListSize; i++) {
			Element xmlElement = sharedElementList.get(i);
			String name = xmlElement.getAttribute(TemplateEngineHelper.ID);

			if (updateName.equals(name)) {
				persistDataMap.put(updateName, updateValue);
			}
		}

		updateShareDefaultsMap(persistDataMap);
	}