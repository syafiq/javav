	public String getMemento() throws CoreException {
		if (getCSourceLocator() != null) {
			Document document = null;
			Throwable ex = null;
			try {
				document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Element element = document.createElement(ELEMENT_NAME);
				document.appendChild(element);
				element.setAttribute(ATTR_PROJECT, getCSourceLocator().getProject().getName());
				IPersistableSourceLocator psl = getPersistableSourceLocator();
				if (psl != null) {
					element.setAttribute(ATTR_MEMENTO, psl.getMemento());
				}
				return CDebugUtils.serializeDocument(document);
			} catch (ParserConfigurationException e) {
				ex = e;
			} catch (IOException e) {
				ex = e;
			} catch (TransformerException e) {
				ex = e;
			}
			abort(SourceLookupMessages.getString("OldDefaultSourceLocator.1"), ex); //$NON-NLS-1$
		}
		return null;
	}