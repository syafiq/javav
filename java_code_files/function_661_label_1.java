	private synchronized void initExtensionPoints() {
		if (storageTypeMap != null)
			return;
		Map<String, List<CProjectDescriptionStorageTypeProxy>> m = new HashMap<>();
		IExtensionPoint extpoint = Platform.getExtensionRegistry().getExtensionPoint(CCorePlugin.PLUGIN_ID,
				CPROJ_DESC_STORAGE_EXT_ID);
		for (IExtension extension : extpoint.getExtensions()) {
			for (IConfigurationElement configEl : extension.getConfigurationElements()) {
				if (configEl.getName().equalsIgnoreCase(CPROJ_STORAGE_TYPE)) {
					CProjectDescriptionStorageTypeProxy type = initStorageType(configEl);
					if (type != null) {
						if (!m.containsKey(type.id))
							m.put(type.id, new LinkedList<CProjectDescriptionStorageTypeProxy>());
						m.get(type.id).add(type);
					}
				}
			}
		}
		storageTypeMap = m;
	}