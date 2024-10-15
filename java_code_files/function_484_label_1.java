	private void save(OutputStream stream) throws CoreException {
		try {
			DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
			DocumentBuilder builder= factory.newDocumentBuilder();
			Document document= builder.newDocument();

			Element rootElement = document.createElement(fRootNodeName);
			document.appendChild(rootElement);

			Iterator<V> values= getValues().iterator();
			while (values.hasNext()) {
				Object object= values.next();
				Element element= document.createElement(fInfoNodeName);
				setAttributes(object, element);
				rootElement.appendChild(element);
			}

			Transformer transformer=TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(stream);

			transformer.transform(source, result);
		} catch (TransformerException | ParserConfigurationException e) {
			throw createException(e, Messages.format(CorextMessages.History_error_serialize, BasicElementLabels.getResourceName(fFileName)));
		}
	}