	private StorableCdtVariables loadMacrosFromStream(InputStream stream, boolean readOnly) {
		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSource = new InputSource(stream);
			Document document = parser.parse(inputSource);
			Element rootElement = document.getDocumentElement();

			if (!StorableCdtVariables.MACROS_ELEMENT_NAME.equals(rootElement.getNodeName()))
				return null;

			return new StorableCdtVariables(new XmlStorageElement(rootElement), readOnly);
		} catch (ParserConfigurationException e) {

		} catch (SAXException e) {

		} catch (IOException e) {

		}

		return null;
	}