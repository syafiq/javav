	public IClasspathEntry decodeClasspathEntry(String encodedEntry) {

		try {
			if (encodedEntry == null) return null;
			StringReader reader = new StringReader(encodedEntry);
			Element node;

			try {
				DocumentBuilder parser =
					DocumentBuilderFactory.newInstance().newDocumentBuilder();
				node = parser.parse(new InputSource(reader)).getDocumentElement();
			} catch (SAXException | ParserConfigurationException e) {
				return null;
			} finally {
				reader.close();
			}

			if (!node.getNodeName().equalsIgnoreCase(ClasspathEntry.TAG_CLASSPATHENTRY)
					|| node.getNodeType() != Node.ELEMENT_NODE) {
				return null;
			}
			return ClasspathEntry.elementDecode(node, this, null/*not interested in unknown elements*/);
		} catch (IOException e) {
			// bad format
			return null;
		}
	}