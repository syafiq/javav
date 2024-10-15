	private void save(TemplatePersistenceData[] templates, StreamResult result) throws IOException {
		try {
			@SuppressWarnings("restriction")
			Document document= org.eclipse.core.internal.runtime.XmlProcessorFactory.newDocumentWithErrorOnDOCTYPE();
			Node root= document.createElement(TEMPLATE_ROOT);
			document.appendChild(root);

			for (TemplatePersistenceData data : templates) {
				Template template= data.getTemplate();

				Node node= document.createElement(TEMPLATE_ELEMENT);
				root.appendChild(node);

				NamedNodeMap attributes= node.getAttributes();

				String id= data.getId();
				if (id != null) {
					Attr idAttr= document.createAttribute(ID_ATTRIBUTE);
					idAttr.setValue(id);
					attributes.setNamedItem(idAttr);
				}

				if (template != null) {
					Attr name= document.createAttribute(NAME_ATTRIBUTE);
					name.setValue(validateXML(template.getName()));
					attributes.setNamedItem(name);
				}

				if (template != null) {
					Attr description= document.createAttribute(DESCRIPTION_ATTRIBUTE);
					description.setValue(validateXML(template.getDescription()));
					attributes.setNamedItem(description);
				}

				if (template != null) {
					Attr context= document.createAttribute(CONTEXT_ATTRIBUTE);
					context.setValue(validateXML(template.getContextTypeId()));
					attributes.setNamedItem(context);
				}

				Attr enabled= document.createAttribute(ENABLED_ATTRIBUTE);
				enabled.setValue(data.isEnabled() ? Boolean.toString(true) : Boolean.toString(false));
				attributes.setNamedItem(enabled);

				Attr deleted= document.createAttribute(DELETED_ATTRIBUTE);
				deleted.setValue(data.isDeleted() ? Boolean.toString(true) : Boolean.toString(false));
				attributes.setNamedItem(deleted);

				if (template != null) {
					Attr autoInsertable= document.createAttribute(AUTO_INSERTABLE_ATTRIBUTE);
					autoInsertable.setValue(template.isAutoInsertable() ? Boolean.toString(true) : Boolean.toString(false));
					attributes.setNamedItem(autoInsertable);
				}

				if (template != null) {
					Text pattern= document.createTextNode(validateXML(template.getPattern()));
					node.appendChild(pattern);
				}
			}
			@SuppressWarnings("restriction")
			Transformer transformer= org.eclipse.core.internal.runtime.XmlProcessorFactory.createTransformerFactoryWithErrorOnDOCTYPE().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
			DOMSource source = new DOMSource(document);

			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			Assert.isTrue(false);
		} catch (TransformerException e) {
			if (e.getException() instanceof IOException)
				throw (IOException) e.getException();
			Assert.isTrue(false);
		}
	}