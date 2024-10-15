	public static Document createDocument(String name) throws ParserConfigurationException {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		doc.appendChild(doc.createElement(name));
		return doc;
	}