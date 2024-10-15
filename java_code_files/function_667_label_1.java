	public WorkspaceLanguageConfiguration decodeWorkspaceMappings() throws CoreException {
		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(CCorePlugin.PLUGIN_ID);
		IEclipsePreferences defaultNode = DefaultScope.INSTANCE.getNode(CCorePlugin.PLUGIN_ID);
		String encodedMappings = node.get(CCorePreferenceConstants.WORKSPACE_LANGUAGE_MAPPINGS, null);
		if (encodedMappings == null) {
			encodedMappings = defaultNode.get(CCorePreferenceConstants.WORKSPACE_LANGUAGE_MAPPINGS, null);
		}
		WorkspaceLanguageConfiguration config = new WorkspaceLanguageConfiguration();

		if (encodedMappings == null || encodedMappings.length() == 0) {
			return config;
		}

		// The mappings are encoded as XML in a String so we need to parse it.
		InputSource input = new InputSource(new StringReader(encodedMappings));
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
			config.setWorkspaceMappings(decodeContentTypeMappings(document.getDocumentElement()));
			return config;
		} catch (SAXException e) {
			throw new CoreException(Util.createStatus(e));
		} catch (IOException e) {
			throw new CoreException(Util.createStatus(e));
		} catch (ParserConfigurationException e) {
			throw new CoreException(Util.createStatus(e));
		}
	}