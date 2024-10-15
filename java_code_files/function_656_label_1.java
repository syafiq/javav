	private ICStorageElement readOldCDTProjectFile(IProject project) throws CoreException {
		ICStorageElement storage = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = null;
			InputStream stream = getSharedProperty(project, OLD_CDTPROJECT_FILE_NAME);
			if (stream != null) {
				doc = builder.parse(stream);
				NodeList nodeList = doc.getElementsByTagName(OLD_PROJECT_DESCRIPTION);

				if (nodeList != null && nodeList.getLength() > 0) {
					Node node = nodeList.item(0);
					storage = new InternalXmlStorageElement((Element) node, false);
				}
			}
		} catch (ParserConfigurationException e) {
			throw ExceptionFactory.createCoreException(e);
		} catch (SAXException e) {
			throw ExceptionFactory.createCoreException(e);
		} catch (IOException e) {
			throw ExceptionFactory.createCoreException(e);
		}
		return storage;
	}