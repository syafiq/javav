	public void saveToStream(OutputStream stream) throws CoreException {
		try {
			DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
			DocumentBuilder builder= factory.newDocumentBuilder();
			Document document= builder.newDocument();

			Node root= document.createElement("templates"); //$NON-NLS-1$
			document.appendChild(root);

			for (Template template : fTemplates) {
				Node node= document.createElement(getTemplateTag());
				root.appendChild(node);

				NamedNodeMap attributes= node.getAttributes();

				Attr name= document.createAttribute(NAME_ATTRIBUTE);
				name.setValue(template.getName());
				attributes.setNamedItem(name);

				Attr description= document.createAttribute(DESCRIPTION_ATTRIBUTE);
				description.setValue(template.getDescription());
				attributes.setNamedItem(description);

				Attr context= document.createAttribute(CONTEXT_ATTRIBUTE);
				context.setValue(template.getContextTypeId());
				attributes.setNamedItem(context);

				Text pattern= document.createTextNode(template.getPattern());
				node.appendChild(pattern);
			}


			Transformer transformer=TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(stream);

			transformer.transform(source, result);

		} catch (ParserConfigurationException | TransformerException e) {
			throwWriteException(e);
		}
	}