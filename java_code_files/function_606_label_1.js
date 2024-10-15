	private TemplatePersistenceData[] read(InputSource source, ResourceBundle bundle, String singleId) throws IOException {
		try {
			Collection<TemplatePersistenceData> templates= new ArrayList<>();
			Set<String> ids= new HashSet<>();

			DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
			DocumentBuilder parser= factory.newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			Document document= parser.parse(source);

			NodeList elements= document.getElementsByTagName(TEMPLATE_ELEMENT);

			int count= elements.getLength();
			for (int i= 0; i != count; i++) {
				Node node= elements.item(i);
				NamedNodeMap attributes= node.getAttributes();

				if (attributes == null)
					continue;

				String id= getStringValue(attributes, ID_ATTRIBUTE, null);
				if (id != null && ids.contains(id)) {
					String PLUGIN_ID= "org.eclipse.jface.text"; //$NON-NLS-1$
					ILog log= ILog.of(Platform.getBundle(PLUGIN_ID));
					String message= NLS.bind(TextTemplateMessages.getString("TemplateReaderWriter.duplicate.id"), id); //$NON-NLS-1$
					log.log(new Status(IStatus.WARNING, PLUGIN_ID, IStatus.OK, message, null));
				} else {
					ids.add(id);
				}

				if (singleId != null && !singleId.equals(id))
					continue;

				boolean deleted = getBooleanValue(attributes, DELETED_ATTRIBUTE, false);

				String name= getStringValue(attributes, NAME_ATTRIBUTE);
				name= translateString(name, bundle);

				String description= getStringValue(attributes, DESCRIPTION_ATTRIBUTE, ""); //$NON-NLS-1$
				description= translateString(description, bundle);

				String context= getStringValue(attributes, CONTEXT_ATTRIBUTE);

				if (name == null || context == null)
					throw new IOException(TextTemplateMessages.getString("TemplateReaderWriter.error.missing_attribute")); //$NON-NLS-1$

				boolean enabled = getBooleanValue(attributes, ENABLED_ATTRIBUTE, true);
				boolean autoInsertable= getBooleanValue(attributes, AUTO_INSERTABLE_ATTRIBUTE, true);

				StringBuilder buffer= new StringBuilder();
				NodeList children= node.getChildNodes();
				for (int j= 0; j != children.getLength(); j++) {
					String value= children.item(j).getNodeValue();
					if (value != null)
						buffer.append(value);
				}
				String pattern= buffer.toString();
				pattern= translateString(pattern, bundle);

				Template template= new Template(name, description, context, pattern, autoInsertable);
				TemplatePersistenceData data= new TemplatePersistenceData(template, enabled, id);
				data.setDeleted(deleted);

				templates.add(data);

				if (singleId != null && singleId.equals(id))
					break;
			}

			return templates.toArray(new TemplatePersistenceData[templates.size()]);

		} catch (ParserConfigurationException e) {
			Assert.isTrue(false);
		} catch (SAXException e) {
			throw (IOException)new IOException("Could not read template file").initCause(e); //$NON-NLS-1$
		}

		return null; // dummy
	}