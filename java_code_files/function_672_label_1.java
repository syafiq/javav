	public ICStorageElement createPreferenceStorage(String key, boolean createEmptyIfNotFound, boolean readOnly)
			throws CoreException {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = null;
			Element element = null;
			InputStream stream = null;

			try {
				stream = getPreferenceProperty(key);
				if (stream != null) {
					doc = builder.parse(stream);

					// Get the first element in the project file
					Node rootElement = doc.getFirstChild();

					if (rootElement.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE) {
						throw ExceptionFactory
								.createCoreException(SettingsModelMessages.getString("CProjectDescriptionManager.2")); //$NON-NLS-1$
					} else {
						String fileVersion = rootElement.getNodeValue();
						Version version = new Version(fileVersion);
						// Make sure that the version is compatible with the manager
						// Version must between min version and current version inclusive
						if (MIN_DESCRIPTION_VERSION.compareTo(version) > 0
								|| DESCRIPTION_VERSION.compareTo(version) < 0) {
							throw ExceptionFactory.createCoreException(
									SettingsModelMessages.getString("CProjectDescriptionManager.3")); //$NON-NLS-1$
						}
					}

					// Now get the project root element (there should be only one)
					NodeList nodes = doc.getElementsByTagName(ROOT_PREFERENCE_ELEMENT);
					if (nodes.getLength() == 0)
						throw ExceptionFactory
								.createCoreException(SettingsModelMessages.getString("CProjectDescriptionManager.4")); //$NON-NLS-1$
					Node node = nodes.item(0);
					if (node.getNodeType() != Node.ELEMENT_NODE)
						throw ExceptionFactory
								.createCoreException(SettingsModelMessages.getString("CProjectDescriptionManager.5")); //$NON-NLS-1$
					element = (Element) node;
				} else if (!createEmptyIfNotFound) {
					throw ExceptionFactory
							.createCoreException(SettingsModelMessages.getString("CProjectDescriptionManager.6")); //$NON-NLS-1$
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

			if (element == null) {
				doc = builder.newDocument();
				ProcessingInstruction instruction = doc.createProcessingInstruction(VERSION_ELEMENT_NAME,
						DESCRIPTION_VERSION.toString());
				doc.appendChild(instruction);
				element = doc.createElement(ROOT_PREFERENCE_ELEMENT);
				doc.appendChild(element);
			}
			return new InternalXmlStorageElement(element, null, false, readOnly);
		} catch (ParserConfigurationException e) {
			throw ExceptionFactory.createCoreException(e);
		}
	}