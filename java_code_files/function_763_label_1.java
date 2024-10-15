	public static String getCommonSourceLocationsMemento(ICSourceLocation[] locations) {
		Document document = null;
		Throwable ex = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element element = document.createElement(NAME_COMMON_SOURCE_LOCATIONS);
			document.appendChild(element);
			saveSourceLocations(document, element, locations);
			return CDebugUtils.serializeDocument(document);
		} catch (ParserConfigurationException e) {
			ex = e;
		} catch (IOException e) {
			ex = e;
		} catch (TransformerException e) {
			ex = e;
		}
		CDebugCorePlugin.log(new Status(IStatus.ERROR, CDebugCorePlugin.getUniqueIdentifier(), 0,
				"Error saving common source settings.", ex)); //$NON-NLS-1$
		return null;
	}