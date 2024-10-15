	public String getMemento() throws CoreException {
		Document document = null;
		Throwable ex = null;
		try {
			document = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE().newDocument();
			Element node = document.createElement(SOURCE_LOCATOR_NAME);
			document.appendChild(node);
			ICSourceLocation[] locations = getSourceLocations();
			saveDisabledGenericSourceLocations(locations, document, node);
			saveAdditionalSourceLocations(locations, document, node);
			node.setAttribute(ATTR_DUPLICATE_FILES, String.valueOf(searchForDuplicateFiles()));
			return CDebugUtils.serializeDocument(document);
		} catch (ParserConfigurationException e) {
			ex = e;
		} catch (IOException e) {
			ex = e;
		} catch (TransformerException e) {
			ex = e;
		}
		abort(InternalSourceLookupMessages.CSourceLocator_0, ex);
		// execution will not reach here
		return null;
	}