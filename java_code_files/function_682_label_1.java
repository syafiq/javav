	private void generateSharedXML(File xmlFile) {
		Document d;
		try {
			d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			TemplateEngineUtil.log(e);
			return;
		}
		Node rootElement = d.appendChild(d.createElement("SharedRoot")); //$NON-NLS-1$

		for (String key : sharedDefaultsMap.keySet()) {
			Element element = (Element) rootElement.appendChild(d.createElement("SharedProperty")); //$NON-NLS-1$
			element.setAttribute(TemplateEngineHelper.ID, key);
			element.setAttribute(TemplateEngineHelper.VALUE, sharedDefaultsMap.get(key));
		}

		DOMSource domSource = new DOMSource(d);
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Result fileResult = new StreamResult(xmlFile);
		try {
			transFactory.newTransformer().transform(domSource, fileResult);
		} catch (Throwable t) {
			TemplateEngineUtil.log(t);
		}
	}