	private ByteArrayOutputStream write(ICStorageElement element) throws CoreException {
		Document doc = ((InternalXmlStorageElement) element).fElement.getOwnerDocument();
		XmlUtil.prettyFormat(doc);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			Transformer transformer = XmlProcessorFactoryCdt.createTransformerFactoryWithErrorOnDOCTYPE()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
			// Indentation is done with XmlUtil.prettyFormat(doc)
			transformer.setOutputProperty(OutputKeys.INDENT, "no"); //$NON-NLS-1$
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(stream);
			transformer.transform(source, result);

			return stream;
		} catch (TransformerConfigurationException e) {
			throw ExceptionFactory.createCoreException(e);
		} catch (TransformerException e) {
			throw ExceptionFactory.createCoreException(e);
		}
	}