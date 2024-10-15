	public String getMemento() {
		String collectData = ""; //$NON-NLS-1$

		try {
			DocumentBuilder docBuilder = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement(COLLECT_ACTION_ELEMENT_NAME);

			// Store the different attributes of this collect action
			rootElement.setAttribute(COLLECT_STRING_ATTR, fCollectString);
			rootElement.setAttribute(COLLECT_AS_STRING_ATTR, Boolean.toString(fCharPtrAsStrings));
			rootElement.setAttribute(COLLECT_AS_STRING_LIMIT_ATTR,
					fCharPtrAsStringsLimit == null ? "" : fCharPtrAsStringsLimit.toString()); //$NON-NLS-1$

			doc.appendChild(rootElement);

			ByteArrayOutputStream s = new ByteArrayOutputStream();

			TransformerFactory factory = XmlProcessorFactoryCdt.createTransformerFactoryWithErrorOnDOCTYPE();
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

			DOMSource source = new DOMSource(doc);
			StreamResult outputTarget = new StreamResult(s);
			transformer.transform(source, outputTarget);

			collectData = s.toString("UTF8"); //$NON-NLS-1$

		} catch (Exception e) {
			GdbPlugin.log(e);
		}
		return collectData;
	}