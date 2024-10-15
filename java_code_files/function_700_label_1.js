	public static List<String> decodeListFromMemento(String memento) {
		List<String> list = new ArrayList<>();

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
					String value = elem.getAttribute(ATTRIBUTE_VALUE);
					if (value != null) {
						list.add(value);
					} else {
						throw new Exception();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}