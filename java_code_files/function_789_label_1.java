	private static boolean updateBuildInfo(IProject project, boolean force) throws CoreException {
		IManagedBuildInfo info = getBuildInfo(project, false);
		if (info == null)
			return true;

		ICProjectDescription projDes = CoreModel.getDefault().getProjectDescription(project);
		projDes = BuildSettingsUtil.synchBuildInfo(info, projDes, force);

		//		try {
		BuildSettingsUtil.checkApplyDescription(project, projDes);
		//		} catch (CoreException e) {
		//			return false;
		//		}
		return true;
		/*
		// Create document
		Exception err = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.newDocument();

			// Get the build information for the project
			ManagedBuildInfo buildInfo = (ManagedBuildInfo) getBuildInfo(project);

			// Save the build info
			if (buildInfo != null &&
					!buildInfo.isReadOnly() &&
					buildInfo.isValid() &&
					(force == true || buildInfo.isDirty())) {
				// For post-2.0 projects, there will be a version
				String projectVersion = buildInfo.getVersion();
				if (projectVersion != null) {
					ProcessingInstruction instruction = doc.createProcessingInstruction(VERSION_ELEMENT_NAME, projectVersion);
					doc.appendChild(instruction);
				}
				Element rootElement = doc.createElement(ROOT_NODE_NAME);
				doc.appendChild(rootElement);
				buildInfo.serialize(doc, rootElement);

				// Transform the document to something we can save in a file
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.METHOD, "xml");	//$NON-NLS-1$
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");	//$NON-NLS-1$
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(stream);
				transformer.transform(source, result);

				// Save the document
				IFile projectFile = project.getFile(SETTINGS_FILE_NAME);
				String utfString = stream.toString("UTF-8");	//$NON-NLS-1$

				if (projectFile.exists()) {
					if (projectFile.isReadOnly()) {
						// If we are not running headless, and there is a UI Window around, grab it
						// and the associated shell
						IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
						if (window == null) {
							IWorkbenchWindow windows[] = PlatformUI.getWorkbench().getWorkbenchWindows();
							window = windows[0];
						}
						Shell shell = null;
						if (window != null) {
							shell = window.getShell();
						}
		                // Inform Eclipse that we are intending to modify this file
						// This will provide the user the opportunity, via UI prompts, to fetch the file from source code control
						// reset a read-only file protection to write etc.
						// If there is no shell, i.e. shell is null, then there will be no user UI interaction
						IStatus status = projectFile.getWorkspace().validateEdit(new IFile[]{projectFile}, shell);
						// If the file is still read-only, then we should not attempt the write, since it will
						// just fail - just throw an exception, to be caught below, and inform the user
						// For other non-successful status, we take our chances, attempt the write, and pass
						// along any exception thrown
						if (!status.isOK()) {
						    if (status.getCode() == IResourceStatus.READ_ONLY_LOCAL) {
						    	stream.close();
		    	                throw new IOException(ManagedMakeMessages.getFormattedString(MANIFEST_ERROR_READ_ONLY, projectFile.getFullPath().toString())); //$NON-NLS-1$
						    }
						}
					}
					projectFile.setContents(new ByteArrayInputStream(utfString.getBytes("UTF-8")), IResource.FORCE, new NullProgressMonitor());	//$NON-NLS-1$
				} else {
					projectFile.create(new ByteArrayInputStream(utfString.getBytes("UTF-8")), IResource.FORCE, new NullProgressMonitor());	//$NON-NLS-1$
				}

				// Close the streams
				stream.close();
			}
		} catch (ParserConfigurationException e) {
			err = e;
		} catch (FactoryConfigurationError e) {
			err = e.getException();
		} catch (TransformerConfigurationException e) {
			err = e;
		} catch (TransformerFactoryConfigurationError e) {
			err = e.getException();
		} catch (TransformerException e) {
			err = e;
		} catch (IOException e) {
			// The save failed
			err = e;
		} catch (CoreException e) {
			// Save to IFile failed
		    err = e;
		}

		if (err != null) {
			// Put out an error message indicating that the attempted write to the .cdtbuild project file failed
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window == null) {
				IWorkbenchWindow windows[] = PlatformUI.getWorkbench().getWorkbenchWindows();
				window = windows[0];
			}

			final Shell shell = window.getShell();
			if (shell != null) {
				final String exceptionMsg = err.getMessage();
				shell.getDisplay().syncExec( new Runnable() {
					public void run() {
						MessageDialog.openError(shell,
								ManagedMakeMessages.getResourceString("ManagedBuildManager.error.write_failed_title"),	//$NON-NLS-1$
								ManagedMakeMessages.getFormattedString(MANIFEST_ERROR_WRITE_FAILED,		//$NON-NLS-1$
										exceptionMsg));
					}
			    } );
			}
		}
		// If we return an honest status when the operation fails, there are instances when the UI behavior
		// is not very good
		// Specifically, if "OK" is clicked by the user from the property page UI, and the return status
		// from this routine is false, the property page UI will not be closed (note: this is Eclispe code) and
		// the OK button will simply be grayed out
		// At this point, the only way out is to click "Cancel" to get the UI to go away; note however that any
		// property page changes will be sticky, in the UI, which is nonintuitive and confusing
		// Therefore, just always return success, i.e. true, from this routine
		return true;
		*/
	}