	public void initializeFromMemento(String memento) throws CoreException {
		Exception ex = null;
		try {
			Element root = null;
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			StringReader reader = new StringReader(memento);
			InputSource source = new InputSource(reader);
			root = parser.parse(source).getDocumentElement();
			if (!root.getNodeName().equalsIgnoreCase(ELEMENT_NAME)) {
				abort(SourceLookupMessages.getString("OldDefaultSourceLocator.2"), null); //$NON-NLS-1$
			}
			String projectName = root.getAttribute(ATTR_PROJECT);
			String data = root.getAttribute(ATTR_MEMENTO);
			if (isEmpty(projectName)) {
				abort(SourceLookupMessages.getString("OldDefaultSourceLocator.3"), null); //$NON-NLS-1$
			}
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			if (getCSourceLocator() == null)
				setCSourceLocator(SourceLookupFactory.createSourceLocator(project));
			if (getCSourceLocator().getProject() != null && !getCSourceLocator().getProject().equals(project))
				return;
			if (project == null || !project.exists() || !project.isOpen())
				abort(MessageFormat.format(SourceLookupMessages.getString("OldDefaultSourceLocator.4"), //$NON-NLS-1$
						new Object[] { projectName }), null);
			IPersistableSourceLocator psl = getPersistableSourceLocator();
			if (psl != null)
				psl.initializeFromMemento(data);
			else
				abort(SourceLookupMessages.getString("OldDefaultSourceLocator.5"), null); //$NON-NLS-1$
			return;
		} catch (ParserConfigurationException e) {
			ex = e;
		} catch (SAXException e) {
			ex = e;
		} catch (IOException e) {
			ex = e;
		}
		abort(SourceLookupMessages.getString("OldDefaultSourceLocator.6"), ex); //$NON-NLS-1$
	}