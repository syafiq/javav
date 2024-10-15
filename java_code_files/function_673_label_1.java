	protected InternalXmlStorageElement createStorage(IContainer container, String fileName, boolean reCreate,
			boolean createEmptyIfNotFound, boolean readOnly) throws CoreException {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = null;
			Element element = null;
			InputStream stream = null;
			if (reCreate) {
				try {
					stream = getSharedProperty(container, fileName);
					if (stream != null) {
						doc = builder.parse(stream);

						// Get the first element in the project file
						Node rootElement = doc.getFirstChild();

						if (rootElement.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE) {
							throw ExceptionFactory.createCoreException(
									SettingsModelMessages.getString("CProjectDescriptionManager.7")); //$NON-NLS-1$
						} else {
							// Make sure that the version is compatible with the manager
							String fileVersion = rootElement.getNodeValue();
							Version version = new Version(fileVersion);
							if (getVersion().compareTo(version) < 0) {
								throw ExceptionFactory.createCoreException(
										SettingsModelMessages.getString("CProjectDescriptionManager.8")); //$NON-NLS-1$
							}
						}

						// Now get the project root element (there should be only one)
						NodeList nodes = doc
								.getElementsByTagName(ICProjectDescriptionStorageType.STORAGE_ROOT_ELEMENT_NAME);
						if (nodes.getLength() == 0)
							throw ExceptionFactory.createCoreException(
									SettingsModelMessages.getString("CProjectDescriptionManager.9")); //$NON-NLS-1$
						Node node = nodes.item(0);
						if (node.getNodeType() != Node.ELEMENT_NODE)
							throw ExceptionFactory.createCoreException(
									SettingsModelMessages.getString("CProjectDescriptionManager.10")); //$NON-NLS-1$
						element = (Element) node;
					} else if (!createEmptyIfNotFound) {
						throw ExceptionFactory.createCoreException(
								SettingsModelMessages.getString("CProjectDescriptionManager.11") + fileName); //$NON-NLS-1$
					}
				} catch (FactoryConfigurationError e) {
					if (!createEmptyIfNotFound)
						throw ExceptionFactory.createCoreException(e.getLocalizedMessage());
				} catch (SAXException e) {
					if (!createEmptyIfNotFound)
						throw ExceptionFactory.createCoreException(e);
				} catch (IOException e) {
					if (!createEmptyIfNotFound)
						throw ExceptionFactory.createCoreException(e);
				} finally {
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e) {
						}
					}
				}
			}

			if (element == null) {
				doc = builder.newDocument();
				ProcessingInstruction instruction = doc.createProcessingInstruction(
						ICProjectDescriptionStorageType.STORAGE_VERSION_NAME, getVersion().toString());
				doc.appendChild(instruction);
				element = doc.createElement(ICProjectDescriptionStorageType.STORAGE_ROOT_ELEMENT_NAME);
				element.setAttribute(ICProjectDescriptionStorageType.STORAGE_TYPE_ATTRIBUTE, getStorageTypeId());
				doc.appendChild(element);
			}

			return new InternalXmlStorageElement(element, null, false, readOnly);
		} catch (ParserConfigurationException e) {
			throw ExceptionFactory.createCoreException(e);
		}
	}