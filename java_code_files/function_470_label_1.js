	public void addFromStream(InputStream stream, boolean allowDuplicates) throws CoreException {
		try {
			DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
			DocumentBuilder parser= factory.newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			Document document= parser.parse(new InputSource(stream));

			NodeList elements= document.getElementsByTagName(getTemplateTag());

			int count= elements.getLength();
			for (int i= 0; i != count; i++) {
				Node node= elements.item(i);
				NamedNodeMap attributes= node.getAttributes();

				if (attributes == null)
					continue;

				String name= getAttributeValue(attributes, NAME_ATTRIBUTE);
				String description= getAttributeValue(attributes, DESCRIPTION_ATTRIBUTE);
				if (name == null || description == null)
					continue;

				String context= getAttributeValue(attributes, CONTEXT_ATTRIBUTE);

				if (context == null)
					throw new SAXException(JavaTemplateMessages.TemplateSet_error_missing_attribute);

				StringBuilder buffer= new StringBuilder();
				NodeList children= node.getChildNodes();
				for (int j= 0; j != children.getLength(); j++) {
					String value= children.item(j).getNodeValue();
					if (value != null)
						buffer.append(value);
				}
				String pattern= buffer.toString().trim();

				Template template= new Template(name, description, context, pattern,  true);

				String message= validateTemplate(template);
				if (message == null) {
					if (!allowDuplicates) {
						for (Template t : getTemplates(name)) {
							remove(t);
						}
					}
					add(template);
				} else {
					throwReadException(null);
				}
			}
		} catch (ParserConfigurationException | IOException | SAXException e) {
			throwReadException(e);
		}
	}