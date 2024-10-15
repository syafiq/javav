	public String getMemento() throws CoreException {
		Document document = null;
		Throwable ex = null;
		try {
			document = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE().newDocument();
			Element node = document.createElement(ELEMENT_NAME);
			document.appendChild(node);
			node.setAttribute(ATTR_PROJECT, getProject().getName());
			node.setAttribute(ATTR_GENERIC, String.valueOf(isGeneric()));
			return CDebugUtils.serializeDocument(document);
		} catch (ParserConfigurationException e) {
			ex = e;
		} catch (IOException e) {
			ex = e;
		} catch (TransformerException e) {
			ex = e;
		}
		abort(NLS.bind(InternalSourceLookupMessages.CProjectSourceLocation_0, getProject().getName()), ex);
		// execution will not reach here
		return null;
	}