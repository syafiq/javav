	private static byte[] toByteArray(Document doc) throws CoreException {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, ENCODING_UTF_8);
			// Indentation is done with XmlUtil.prettyFormat(doc).
			transformer.setOutputProperty(OutputKeys.INDENT, "no"); //$NON-NLS-1$
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(stream);
			transformer.transform(source, result);

			return stream.toByteArray();
		} catch (Exception e) {
			throw new CoreException(CCorePlugin.createStatus(Messages.XmlUtil_InternalErrorSerializing, e));
		}
	}