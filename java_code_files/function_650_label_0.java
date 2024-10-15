	private ByteArrayOutputStream storeEnvironmentToStream(StorableEnvironment env) throws CoreException {
		try {
			DocumentBuilder builder = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			Document document = builder.newDocument();

			Element el = document.createElement(StorableEnvironment.ENVIRONMENT_ELEMENT_NAME);
			document.appendChild(el);
			XmlStorageElement rootElement = new XmlStorageElement(el);
			env.serialize(rootElement);

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
			throw new CoreException(new Status(IStatus.ERROR, CCorePlugin.PLUGIN_ID, -1, e.getMessage(), e));
		} catch (TransformerConfigurationException e) {
			throw new CoreException(new Status(IStatus.ERROR, CCorePlugin.PLUGIN_ID, -1, e.getMessage(), e));
		} catch (TransformerException e) {
			throw new CoreException(new Status(IStatus.ERROR, CCorePlugin.PLUGIN_ID, -1, e.getMessage(), e));
		}
	}