	public void storeMappings(WorkspaceLanguageConfiguration config) throws CoreException {
		try {
			// Encode mappings as XML and serialize as a String.
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element rootElement = doc.createElement(WORKSPACE_MAPPINGS);
			doc.appendChild(rootElement);
			addContentTypeMappings(config.getWorkspaceMappings(), rootElement);
			Transformer serializer = createSerializer();
			DOMSource source = new DOMSource(doc);
			StringWriter buffer = new StringWriter();
			StreamResult result = new StreamResult(buffer);
			serializer.transform(source, result);
			String encodedMappings = buffer.getBuffer().toString();

			IEclipsePreferences node = InstanceScope.INSTANCE.getNode(CCorePlugin.PLUGIN_ID);
			node.put(CCorePreferenceConstants.WORKSPACE_LANGUAGE_MAPPINGS, encodedMappings);
			node.flush();
		} catch (ParserConfigurationException e) {
			throw new CoreException(Util.createStatus(e));
		} catch (TransformerException e) {
			throw new CoreException(Util.createStatus(e));
		} catch (BackingStoreException e) {
			throw new CoreException(Util.createStatus(e));
		}
	}