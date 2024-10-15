	public static String encodeListIntoMemento(List<String> labels) {
		String returnValue = null;

		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = dfactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement(ROOT_ELEMENT_TAGNAME);
			doc.appendChild(rootElement);
			// create one XML element per list entry to save
			for (String lbl : labels) {
				Element elem = doc.createElement(ELEMENT_TAGNAME);
				elem.setAttribute(ATTRIBUTE_VALUE, lbl);
				rootElement.appendChild(elem);
			}

			ByteArrayOutputStream s = new ByteArrayOutputStream();

			TransformerFactory factory = TransformerFactory.newInstance();
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