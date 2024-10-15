	public static String serializeDocument(Document doc, boolean indent) throws IOException, TransformerException {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		TransformerFactory factory = XmlProcessorFactoryCdt.createTransformerFactoryWithErrorOnDOCTYPE();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
		transformer.setOutputProperty(OutputKeys.INDENT, indent ? "yes" : "no"); //$NON-NLS-1$ //$NON-NLS-2$
		DOMSource source = new DOMSource(doc);
		StreamResult outputTarget = new StreamResult(s);
		transformer.transform(source, outputTarget);
		return s.toString("UTF8"); //$NON-NLS-1$
	}