	public void beginRefactoring(final String id, long stamp, final String project, final String description, final String comment, final int flags) throws CoreException {
		Assert.isNotNull(id);
		Assert.isNotNull(description);
		Assert.isTrue(flags >= RefactoringDescriptor.NONE);
		try {
			if (fDocument == null)
				fDocument= DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException | FactoryConfigurationError exception) {
			throw new CoreException(new Status(IStatus.ERROR, RefactoringCorePlugin.getPluginId(), IRefactoringCoreStatusCodes.REFACTORING_HISTORY_IO_ERROR, exception.getLocalizedMessage(), null));
		}
		if (fRefactoring == null) {
			try {
				fRefactoringArguments= new ArrayList<>(16);
				fRefactoring= fDocument.createElement(IRefactoringSerializationConstants.ELEMENT_REFACTORING);
				Attr attribute= fDocument.createAttribute(IRefactoringSerializationConstants.ATTRIBUTE_ID);
				attribute.setValue(id);
				fRefactoringArguments.add(attribute);
				if (stamp >= 0) {
					attribute= fDocument.createAttribute(IRefactoringSerializationConstants.ATTRIBUTE_STAMP);
					attribute.setValue(Long.toString(stamp));
					fRefactoringArguments.add(attribute);
				}
				if (flags != RefactoringDescriptor.NONE) {
					attribute= fDocument.createAttribute(IRefactoringSerializationConstants.ATTRIBUTE_FLAGS);
					attribute.setValue(String.valueOf(flags));
					fRefactoringArguments.add(attribute);
				}
				attribute= fDocument.createAttribute(IRefactoringSerializationConstants.ATTRIBUTE_DESCRIPTION);
				attribute.setValue(description);
				fRefactoringArguments.add(attribute);
				if (comment != null && !"".equals(comment)) { //$NON-NLS-1$
					attribute= fDocument.createAttribute(IRefactoringSerializationConstants.ATTRIBUTE_COMMENT);
					attribute.setValue(comment);
					fRefactoringArguments.add(attribute);
				}
				if (project != null && fProjects) {
					attribute= fDocument.createAttribute(IRefactoringSerializationConstants.ATTRIBUTE_PROJECT);
					attribute.setValue(project);
					fRefactoringArguments.add(attribute);
				}
				if (fSession == null)
					fDocument.appendChild(fRefactoring);
				else
					fSession.appendChild(fRefactoring);
			} catch (DOMException exception) {
				throw new CoreException(new Status(IStatus.ERROR, RefactoringCorePlugin.getPluginId(), IRefactoringCoreStatusCodes.REFACTORING_HISTORY_FORMAT_ERROR, exception.getLocalizedMessage(), null));
			}
		}
	}