	private void load(InputSource inputSource) throws CoreException {
		Element root;
		try {
			DocumentBuilder parser = XmlProcessorFactoryJdtUi.createDocumentBuilderFactoryWithErrorOnDOCTYPE().newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			root = parser.parse(inputSource).getDocumentElement();
		} catch (SAXException | ParserConfigurationException | IOException e) {
			throw createException(e, Messages.format(CorextMessages.History_error_read, BasicElementLabels.getResourceName(fFileName)));
		}

		if (root == null) return;
		if (!root.getNodeName().equalsIgnoreCase(fRootNodeName)) {
			return;
		}
		NodeList list= root.getChildNodes();
		int length= list.getLength();
		for (int i= 0; i < length; ++i) {
			Node node= list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element type= (Element) node;
				if (type.getNodeName().equalsIgnoreCase(fInfoNodeName)) {
					V object= createFromElement(type);
					if (object != null) {
						fHistory.put(getKey(object), object);
					}
				}
			}
		}
		rebuildPositions();
	}