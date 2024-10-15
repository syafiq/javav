	public static void writeDocument(Element javadocElement, String encoding, OutputStream outputStream) throws TransformerException {

		// Write the document to the stream
		Transformer transformer=TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
		transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4"); //$NON-NLS-1$ //$NON-NLS-2$
		DOMSource source = new DOMSource(javadocElement.getOwnerDocument());
		StreamResult result = new StreamResult(new BufferedOutputStream(outputStream));
		transformer.transform(source, result);

	}