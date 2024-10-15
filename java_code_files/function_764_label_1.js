	public static ICSourceLocation[] getCommonSourceLocationsFromMemento(String memento) {
		ICSourceLocation[] result = new ICSourceLocation[0];
		if (!isEmpty(memento)) {
			try {
				DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				StringReader reader = new StringReader(memento);
				InputSource source = new InputSource(reader);
				Element root = parser.parse(source).getDocumentElement();
				if (root.getNodeName().equalsIgnoreCase(NAME_COMMON_SOURCE_LOCATIONS))
					result = initializeSourceLocations(root);
			} catch (ParserConfigurationException e) {
				CDebugCorePlugin.log(new Status(IStatus.ERROR, CDebugCorePlugin.getUniqueIdentifier(), 0,
						"Error initializing common source settings.", e)); //$NON-NLS-1$
			} catch (SAXException e) {
				CDebugCorePlugin.log(new Status(IStatus.ERROR, CDebugCorePlugin.getUniqueIdentifier(), 0,
						"Error initializing common source settings.", e)); //$NON-NLS-1$
			} catch (IOException e) {
				CDebugCorePlugin.log(new Status(IStatus.ERROR, CDebugCorePlugin.getUniqueIdentifier(), 0,
						"Error initializing common source settings.", e)); //$NON-NLS-1$
			}
		}
		return result;
	}