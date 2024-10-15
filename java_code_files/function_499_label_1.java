	public void beginSession(final String comment, final String version) throws CoreException {
		if (fDocument == null) {
			try {
				fDocument= DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				fSession= fDocument.createElement(IRefactoringSerializationConstants.ELEMENT_SESSION);
				fSessionArguments= new ArrayList<>(2);
				Attr attribute= fDocument.createAttribute(IRefactoringSerializationConstants.ATTRIBUTE_VERSION);
				attribute.setValue(version);
				fSessionArguments.add(attribute);
				if (comment != null && !"".equals(comment)) { //$NON-NLS-1$
					attribute= fDocument.createAttribute(IRefactoringSerializationConstants.ATTRIBUTE_COMMENT);
					attribute.setValue(comment);
					fSessionArguments.add(attribute);
				}
				fDocument.appendChild(fSession);
			} catch (DOMException exception) {
				throw new CoreException(new Status(IStatus.ERROR, RefactoringCorePlugin.getPluginId(), IRefactoringCoreStatusCodes.REFACTORING_HISTORY_FORMAT_ERROR, exception.getLocalizedMessage(), null));
			} catch (ParserConfigurationException exception) {
				throw new CoreException(new Status(IStatus.ERROR, RefactoringCorePlugin.getPluginId(), IRefactoringCoreStatusCodes.REFACTORING_HISTORY_IO_ERROR, exception.getLocalizedMessage(), null));
			}
		}
	}