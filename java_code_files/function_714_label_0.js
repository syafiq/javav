	public static String encodeMapIntoMemento(Map<String, String> keyPairValues) {
		String returnValue = null;

		try {
			DocumentBuilder docBuilder = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement(ROOT_ELEMENT_TAGNAME);
			doc.appendChild(rootElement);
			// create one XML element per map entry
			for (String key : keyPairValues.keySet()) {
				Element elem = doc.createElement(ELEMENT_TAGNAME);
				// store key and value as values of 2 attributes
				elem.setAttribute(ATTRIBUTE_KEY, key);
				elem.setAttribute(ATTRIBUTE_VALUE, keyPairValues.get(key));
				rootElement.appendChild(elem);
			}

			ByteArrayOutputStream s = new ByteArrayOutputStream();

			TransformerFactory factory = XmlProcessorFactoryCdt.createTransformerFactoryWithErrorOnDOCTYPE();
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

			DOMSource source = new DOMSource(doc);
			StreamResult outputTarget = new StreamResult(s);
			transformer.transform(source, outputTarget);

			returnValue = s.toString("UTF8"); //$NON-NLS-1$
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}