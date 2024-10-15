	public void load(Reader r) {
		Document document = null;
		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			//		parser.setProcessNamespace(true);
			document = parser.parse(new InputSource(r));

			//Strip out any comments first
			Node root = document.getFirstChild();
			while (root.getNodeType() == Node.COMMENT_NODE) {
				document.removeChild(root);
				root = document.getFirstChild();
			}
			load(document, (Element) root);
		} catch (ParserConfigurationException | IOException | SAXException e) {
			// ignore
		}
	}