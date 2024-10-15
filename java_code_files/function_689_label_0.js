	public String toString() {
		StringBuilder builder = new StringBuilder();
		synchronized (fLock) {
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				Transformer transformer = XmlProcessorFactoryCdt.createTransformerFactoryWithErrorOnDOCTYPE()
						.newTransformer();
				// Indentation is done with XmlUtil.prettyFormat(doc).
				// For debugging, the prettyFormat may not have been run yet,
				// so turning this to "yes" may be helpful on occasion.
				transformer.setOutputProperty(OutputKeys.INDENT, "no"); //$NON-NLS-1$

				DOMSource source = new DOMSource(fElement);
				StreamResult result = new StreamResult(stream);
				transformer.transform(source, result);
				builder.append(stream.toString());

			} catch (Exception e) {
				return fElement.toString();
			}
		}
		return builder.toString();
	}