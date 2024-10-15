	public static Document convertModel(Iterable<? extends javax.lang.model.element.Element> declarations) throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document model = factory.newDocumentBuilder().newDocument();
		org.w3c.dom.Element modelNode = model.createElement(MODEL_TAG);

		XMLConverter converter = new XMLConverter(model);
		converter.scan(declarations, modelNode);
		model.appendChild(modelNode);
		return model;
	}