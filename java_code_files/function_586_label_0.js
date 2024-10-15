	public static Map<FactoryContainer, FactoryPath.Attributes> decodeFactoryPath(final String xmlFactoryPath)
	throws CoreException
	{
		Map<FactoryContainer, FactoryPath.Attributes> result = new LinkedHashMap<>();
		StringReader reader = new StringReader(xmlFactoryPath);
		Element fpElement = null;

		try {
			@SuppressWarnings("restriction")
			DocumentBuilder parser = org.eclipse.core.internal.runtime.XmlProcessorFactory
					.createDocumentBuilderWithErrorOnDOCTYPE();
			fpElement = parser.parse(new InputSource(reader)).getDocumentElement();

		}
		catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, AptPlugin.PLUGIN_ID, -1, Messages.FactoryPathUtil_status_ioException, e));
		}
		catch (SAXException e) {
			throw new CoreException(new Status(IStatus.ERROR, AptPlugin.PLUGIN_ID, -1, Messages.FactoryPathUtil_status_couldNotParse, e));
		}
		catch (ParserConfigurationException e) {
			throw new CoreException(new Status(IStatus.ERROR, AptPlugin.PLUGIN_ID, -1, Messages.FactoryPathUtil_status_parserConfigError, e));
		}
		finally {
			reader.close();
		}

		if (!fpElement.getNodeName().equalsIgnoreCase(FACTORYPATH_TAG)) {
			IOException e = new IOException("Incorrect file format. File must begin with " + FACTORYPATH_TAG); //$NON-NLS-1$
			throw new CoreException(new Status(IStatus.ERROR, AptPlugin.PLUGIN_ID, -1, Messages.FactoryPathUtil_status_ioException, e));
		}
		NodeList nodes = fpElement.getElementsByTagName(FACTORYPATH_ENTRY_TAG);
		for (int i=0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				String kindString = element.getAttribute(KIND);
				// deprecated container type "JAR" is now "EXTJAR"
				if ("JAR".equals(kindString)) { //$NON-NLS-1$
					kindString = "EXTJAR"; //$NON-NLS-1$
				}
				String idString = element.getAttribute(ID);
				String enabledString = element.getAttribute(ENABLED);
				String runInAptModeString = element.getAttribute(RUN_IN_BATCH_MODE);
				FactoryType kind = FactoryType.valueOf(kindString);
				FactoryContainer container = null;
				switch (kind) {

				case WKSPJAR :
					container = newWkspJarFactoryContainer(new Path(idString));
					break;

				case EXTJAR :
					container = newExtJarFactoryContainer(new File(idString));
					break;

				case VARJAR :
					container = newVarJarFactoryContainer(new Path(idString));
					break;

				case PLUGIN :
					container = FactoryPluginManager.getPluginFactoryContainer(idString);
					break;

				default :
					throw new IllegalStateException("Unrecognized kind: " + kind + ". Original string: " + kindString); //$NON-NLS-1$ //$NON-NLS-2$
				}

				if (null != container) {
					FactoryPath.Attributes a = new FactoryPath.Attributes(
							Boolean.parseBoolean(enabledString), Boolean.parseBoolean(runInAptModeString));
					result.put(container, a);
				}
			}
		}

		return result;
	}