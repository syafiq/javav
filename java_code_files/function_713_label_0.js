	public String getMemento() {
		String reverseDebugData = ""; //$NON-NLS-1$

		try {
			DocumentBuilder docBuilder = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("reverseDebugData"); //$NON-NLS-1$
			rootElement.setAttribute("operation", fOperation.toString()); //$NON-NLS-1$

			doc.appendChild(rootElement);

			ByteArrayOutputStream s = new ByteArrayOutputStream();

			TransformerFactory factory = XmlProcessorFactoryCdt.createTransformerFactoryWithErrorOnDOCTYPE();
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

			DOMSource source = new DOMSource(doc);
			StreamResult outputTarget = new StreamResult(s);
			transformer.transform(source, outputTarget);

			reverseDebugData = s.toString("UTF8"); //$NON-NLS-1$

		} catch (Exception e) {
			e.printStackTrace();
		}
		return reverseDebugData;

	}