	static private String docToString(Document doc) throws TransformerException {
		final TransformerFactory tf = TransformerFactory.newInstance();
		final Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); //$NON-NLS-1$
		final StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		final String output = writer.getBuffer().toString().replaceAll("\n|\r", ""); //$NON-NLS-1$ //$NON-NLS-2$
		return output;
	}