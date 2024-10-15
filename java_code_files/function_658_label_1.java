	private static CHelpSettings createHelpSettings(IProject project) {
		//		String projectName = project.getName();
		File file = getSettingsFile();
		CHelpSettings settings = null;
		Element rootElement = null;

		if (file.isFile()) {
			try {
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = builder.parse(file);
				NodeList nodes = doc.getElementsByTagName(ELEMENT_ROOT);

				if (nodes.getLength() > 0)
					rootElement = (Element) nodes.item(0);

			} catch (ParserConfigurationException e) {
			} catch (SAXException e) {
			} catch (IOException e) {
			}
		}

		settings = new CHelpSettings(project, rootElement);
		return settings;
	}