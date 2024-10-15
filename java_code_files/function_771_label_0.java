	private ByteArrayOutputStream storeMacrosToStream(StorableCdtVariables macros) throws CoreException {
		try {
			DocumentBuilder builder = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			Document document = builder.newDocument();

			Element rootElement = document.createElement(StorableCdtVariables.MACROS_ELEMENT_NAME);
			document.appendChild(rootElement);
			ICStorageElement storageElement = new XmlStorageElement(rootElement);
			macros.serialize(storageElement);

			Transformer transformer = XmlProcessorFactoryCdt.createTransformerFactoryWithErrorOnDOCTYPE()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			DOMSource source = new DOMSource(document);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(stream);

			transformer.transform(source, result);
			return stream;
		} catch (ParserConfigurationException e) {
			throw ExceptionFactory.createCoreException(e.getMessage(), e);
		} catch (TransformerConfigurationException e) {
			throw ExceptionFactory.createCoreException(e.getMessage(), e);
		} catch (TransformerException e) {
			throw ExceptionFactory.createCoreException(e.getMessage(), e);
		}
	}