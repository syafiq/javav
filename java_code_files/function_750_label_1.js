	public void initializeFrom(String memento) throws CoreException {
		Exception ex = null;
		try {
			Element root = null;
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			StringReader reader = new StringReader(memento);
			InputSource source = new InputSource(reader);
			root = parser.parse(source).getDocumentElement();

			String dir = root.getAttribute(ATTR_DIRECTORY);
			if (isEmpty(dir)) {
				abort(InternalSourceLookupMessages.CDirectorySourceLocation_1, null);
			} else {
				IPath path = new Path(dir);
				if (path.isValidPath(dir) && path.toFile().isDirectory() && path.toFile().exists()) {
					setDirectory(path);
				} else {
					abort(NLS.bind(InternalSourceLookupMessages.CDirectorySourceLocation_2, dir), null);
				}
			}
			dir = root.getAttribute(ATTR_ASSOCIATION);
			if (isEmpty(dir)) {
				setAssociation(null);
			} else {
				IPath path = new Path(dir);
				if (path.isValidPath(dir)) {
					setAssociation(path);
				} else {
					setAssociation(null);
				}
			}
			setSearchSubfolders(Boolean.valueOf(root.getAttribute(ATTR_SEARCH_SUBFOLDERS)).booleanValue());
			return;
		} catch (ParserConfigurationException e) {
			ex = e;
		} catch (SAXException e) {
			ex = e;
		} catch (IOException e) {
			ex = e;
		}
		abort(InternalSourceLookupMessages.CDirectorySourceLocation_3, ex);
	}