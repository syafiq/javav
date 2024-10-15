	static ICStorageElement environmentStorageFromString(String env) {
		if (env == null)
			return null;
		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSource = new InputSource(new ByteArrayInputStream(env.getBytes()));
			Document document = parser.parse(inputSource);
			Element el = document.getDocumentElement();
			XmlStorageElement rootElement = new XmlStorageElement(el);

			if (!StorableEnvironment.ENVIRONMENT_ELEMENT_NAME.equals(rootElement.getName()))
				return null;

			return rootElement;
		} catch (ParserConfigurationException e) {
			CCorePlugin.log(e);
		} catch (SAXException e) {
			CCorePlugin.log(e);
		} catch (IOException e) {
			CCorePlugin.log(e);
		}
		return null;
	}