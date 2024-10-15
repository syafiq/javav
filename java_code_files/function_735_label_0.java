	public String getMemento() throws CoreException {
		Document document = null;
		Throwable ex = null;
		try {
			document = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE().newDocument();
			Element node = document.createElement(ELEMENT_NAME);
			document.appendChild(node);
			node.setAttribute(ATTR_DIRECTORY, getDirectory().toOSString());
			if (getAssociation() != null)
				node.setAttribute(ATTR_ASSOCIATION, getAssociation().toOSString());
			node.setAttribute(ATTR_SEARCH_SUBFOLDERS, String.valueOf(searchSubfolders()));
			return CDebugUtils.serializeDocument(document);
		} catch (ParserConfigurationException e) {
			ex = e;
		} catch (IOException e) {
			ex = e;
		} catch (TransformerException e) {
			ex = e;
		}
		abort(NLS.bind(InternalSourceLookupMessages.CDirectorySourceLocation_0, getDirectory().toOSString()), ex);
		// execution will not reach here
		return null;
	}