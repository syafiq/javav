	public XMLDumper(Object obj) throws ParserConfigurationException {
		document = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE().newDocument();
		document.appendChild(createObject(obj));
	}