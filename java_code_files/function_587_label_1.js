	public static XMLMemento createWriteRoot(String type) throws DOMException {
		Document document;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element element = document.createElement(type);
			document.appendChild(element);
			return new XMLMemento(document, element);
		} catch (ParserConfigurationException e) {
//            throw new Error(e);
			throw new Error(e.getMessage());
		}
	}