	public void deleteBackEndStorage(String[] deleteName) {
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(parsedXML.toURI().toURL().openStream());
		} catch (Exception exp) {
			TemplateEngineUtil.log(exp);
		}

		List<Element> sharedElementList = TemplateEngine.getChildrenOfElement(document.getDocumentElement());
		int elementListSize = sharedElementList.size();
		for (int i = 0; i < elementListSize; i++) {

			Element xmlElement = sharedElementList.get(i);
			String name = xmlElement.getAttribute(TemplateEngineHelper.ID);

			for (int k = 0; k < deleteName.length; k++) {
				if (deleteName[k].equals(name)) {
					xmlElement.removeAttribute(name);
					sharedDefaultsMap.remove(name);
				}
			}
		}

		updateShareDefaultsMap(sharedDefaultsMap);
	}