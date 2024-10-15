	public static Document createDocument(String name) throws ParserConfigurationException {
		Document doc = org.eclipse.core.internal.runtime.XmlProcessorFactory.newDocumentWithErrorOnDOCTYPE();
		doc.appendChild(doc.createElement(name));
		return doc;
	}