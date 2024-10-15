	public void initializeFromMemento(String memento) throws CoreException {
		Exception ex = null;
		try {
			Element root = null;
			DocumentBuilder parser = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			StringReader reader = new StringReader(memento);
			InputSource source = new InputSource(reader);
			root = parser.parse(source).getDocumentElement();
			if (!root.getNodeName().equalsIgnoreCase(SOURCE_LOCATOR_NAME)) {
				abort(InternalSourceLookupMessages.CSourceLocator_1, null);
			}
			List<ICSourceLocation> sourceLocations = new ArrayList<>();
			// Add locations based on referenced projects
			IProject project = getProject();
			if (project != null && project.exists() && project.isOpen())
				sourceLocations.addAll(Arrays.asList(getDefaultSourceLocations()));
			removeDisabledLocations(root, sourceLocations);
			addAdditionalLocations(root, sourceLocations);
			// To support old launch configuration
			addOldLocations(root, sourceLocations);
			setSourceLocations(sourceLocations.toArray(new ICSourceLocation[sourceLocations.size()]));
			setSearchForDuplicateFiles(Boolean.valueOf(root.getAttribute(ATTR_DUPLICATE_FILES)).booleanValue());
			return;
		} catch (ParserConfigurationException e) {
			ex = e;
		} catch (SAXException e) {
			ex = e;
		} catch (IOException e) {
			ex = e;
		}
		abort(InternalSourceLookupMessages.CSourceLocator_2, ex);
	}