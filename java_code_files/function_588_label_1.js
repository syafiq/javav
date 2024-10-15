	private Document getDom(Reader reader) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(reader));
		} catch (ParserConfigurationException | IOException | SAXException e) {
			throw new IllegalArgumentException(e);
		} finally {
			close(reader);
		}
	}