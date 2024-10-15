	private AbstractCProjectDescriptionStorage loadProjectStorage(IProject project) {
		if (storageTypeMap == null)
			initExtensionPoints();

		// If no project description found, then use the default
		Version version = DEFAULT_STORAGE_VERSION;
		String storageTypeID = DEFAULT_STORAGE_TYPE;

		InputStream stream = null;
		try {
			DocumentBuilder builder = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			stream = getInputStreamForIFile(project, ICProjectDescriptionStorageType.STORAGE_FILE_NAME);
			if (stream != null) {
				Document doc = builder.parse(stream);
				// Get the first element in the project file
				Node rootElement = doc.getFirstChild();
				if (rootElement.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE)
					throw ExceptionFactory
							.createCoreException(SettingsModelMessages.getString("CProjectDescriptionManager.7")); //$NON-NLS-1$
				else
					version = new Version(rootElement.getNodeValue());

				// Now get the project root element (there should be only one)
				NodeList nodes = doc.getElementsByTagName(ICProjectDescriptionStorageType.STORAGE_ROOT_ELEMENT_NAME);
				if (nodes.getLength() == 0)
					throw ExceptionFactory
							.createCoreException(SettingsModelMessages.getString("CProjectDescriptionManager.9")); //$NON-NLS-1$
				Node node = nodes.item(0);
				if (node.getNodeType() != Node.ELEMENT_NODE)
					throw ExceptionFactory
							.createCoreException(SettingsModelMessages.getString("CProjectDescriptionManager.10")); //$NON-NLS-1$
				// If we've got this far, then we're at least dealing with an old style project:
				//  as this didn't use to provide a type, specify one explicitly
				// Choose new style -- separated out storage modules by default as this is backwards compatible...
				storageTypeID = XmlProjectDescriptionStorage2.STORAGE_TYPE_ID;
				if (((Element) node).hasAttribute(ICProjectDescriptionStorageType.STORAGE_TYPE_ATTRIBUTE))
					storageTypeID = ((Element) node)
							.getAttribute(ICProjectDescriptionStorageType.STORAGE_TYPE_ATTRIBUTE);
			}
		} catch (Exception e) {
			// Catch all, if not found, we use the old-style defaults...
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}

		List<CProjectDescriptionStorageTypeProxy> types = storageTypeMap.get(storageTypeID);
		if (types != null) {
			for (CProjectDescriptionStorageTypeProxy type : types) {
				if (type.isCompatible(version)) {
					return type.getProjectDescriptionStorage(type, project, version);
				}
			}
		}

		// No type found!
		CCorePlugin
				.log("CProjectDescriptionStorageType: " + storageTypeID + "  for version: " + version + " not found!"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		return null;
	}