	public static Map<String, String> decodeMapFromMemento(String memento) {
		Map<String, String> keyPairValues = new HashMap<>();

		Element root = null;
		DocumentBuilder parser;
		try {
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			root = parser.parse(new InputSource(new StringReader(memento))).getDocumentElement();
			NodeList nodeList = root.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) node;
					NamedNodeMap nodeMap = elem.getAttributes();
					String key = null;
					String value = null;
					for (int idx = 0; idx < nodeMap.getLength(); idx++) {
						Node attrNode = nodeMap.item(idx);
						if (attrNode.getNodeType() == Node.ATTRIBUTE_NODE) {
							Attr attr = (Attr) attrNode;
							if (attr.getName().equals(ATTRIBUTE_KEY)) {
								key = attr.getValue();
							} else if (attr.getName().equals(ATTRIBUTE_VALUE)) {
								value = attr.getValue();
							}
						}
					}
					if (key != null && value != null) {
						keyPairValues.put(key, value);
					} else {
						throw new Exception();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyPairValues;
	}