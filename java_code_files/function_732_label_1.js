	public void serialize(ICHelpInvocationContext context) {
		CHelpSettings settings = getHelpSettings(context);

		File file = getSettingsFile();

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc;
			Element rootElement = null;

			if (file.exists()) {
				doc = builder.parse(file);
				NodeList nodes = doc.getElementsByTagName(ELEMENT_ROOT);

				if (nodes.getLength() > 0)
					rootElement = (Element) nodes.item(0);
			} else {
				doc = builder.newDocument();
			}

			if (rootElement == null) {
				rootElement = doc.createElement(ELEMENT_ROOT);
				doc.appendChild(rootElement);
			}

			settings.serialize(doc, rootElement);

			FileWriter writer = new FileWriter(file);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(writer);

			transformer.transform(source, result);

			writer.close();
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (TransformerConfigurationException e) {
		} catch (TransformerException e) {
		} catch (IOException e) {
		}
	}