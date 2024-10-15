	public XMLDumper(Object obj) throws ParserConfigurationException {
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		document.appendChild(createObject(obj));
	}