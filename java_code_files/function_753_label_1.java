	public void initializeFrom(String memento) throws CoreException {
		Exception ex = null;
		try {
			Element root = null;
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			StringReader reader = new StringReader(memento);
			InputSource source = new InputSource(reader);
			root = parser.parse(source).getDocumentElement();

			String name = root.getAttribute(ATTR_PROJECT);
			if (isEmpty(name)) {
				abort(InternalSourceLookupMessages.CProjectSourceLocation_1, null);
			} else {
				IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
				setProject(project);
			}
			String isGeneric = root.getAttribute(ATTR_GENERIC);
			if (isGeneric == null || isGeneric.trim().length() == 0)
				isGeneric = Boolean.FALSE.toString();
			setGenerated(isGeneric.equals(Boolean.TRUE.toString()));
			return;
		} catch (ParserConfigurationException e) {
			ex = e;
		} catch (SAXException e) {
			ex = e;
		} catch (IOException e) {
			ex = e;
		}
		abort(InternalSourceLookupMessages.CProjectSourceLocation_2, ex);
	}