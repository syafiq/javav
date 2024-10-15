	public RefactoringSessionDescriptor readSession(final InputSource source) throws CoreException {
		fSessionFound= false;
		try {
			source.setSystemId("/"); //$NON-NLS-1$
			createParser(SAXParserFactory.newInstance()).parse(source, this);
			if (!fSessionFound)
				throw new CoreException(new Status(IStatus.ERROR, RefactoringCorePlugin.getPluginId(), IRefactoringCoreStatusCodes.REFACTORING_HISTORY_FORMAT_ERROR, RefactoringCoreMessages.RefactoringSessionReader_no_session, null));
			if (fRefactoringDescriptors != null) {
				if (fVersion == null || "".equals(fVersion)) //$NON-NLS-1$
					throw new CoreException(new Status(IStatus.ERROR, RefactoringCorePlugin.getPluginId(), IRefactoringCoreStatusCodes.MISSING_REFACTORING_HISTORY_VERSION, RefactoringCoreMessages.RefactoringSessionReader_missing_version_information, null));
				if (!IRefactoringSerializationConstants.CURRENT_VERSION.equals(fVersion))
					throw new CoreException(new Status(IStatus.ERROR, RefactoringCorePlugin.getPluginId(), IRefactoringCoreStatusCodes.UNSUPPORTED_REFACTORING_HISTORY_VERSION, RefactoringCoreMessages.RefactoringSessionReader_unsupported_version_information, null));
				return new RefactoringSessionDescriptor(fRefactoringDescriptors.toArray(new RefactoringDescriptor[fRefactoringDescriptors.size()]), fVersion, fComment);
			}
		} catch (SAXParseException exception) {
			String message= Messages.format(RefactoringCoreMessages.RefactoringSessionReader_invalid_contents_at,
					new Object[] {
							Integer.toString(exception.getLineNumber()),
							Integer.toString(exception.getColumnNumber())
			});
			throwCoreException(exception, message);
		} catch (IOException | ParserConfigurationException | SAXException exception) {
			throwCoreException(exception, exception.getLocalizedMessage());
		} finally {
			fRefactoringDescriptors= null;
			fVersion= null;
			fComment= null;
			fLocator= null;
		}
		return null;
	}