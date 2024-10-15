	private void transformDocument(Writer writer) {
		try {
			DOMSource source = new DOMSource(this.document);
			TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(writer));
		} catch (TransformerException e) {
			throw new IllegalStateException(e);
		} finally {
			close(writer);
		}
	}