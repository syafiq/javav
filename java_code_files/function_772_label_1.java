	public static void doProjectUpdate(IProgressMonitor monitor, final IProject project) throws CoreException {
		String[] projectName = new String[] { project.getName() };
		IFile file = project.getFile(ManagedBuildManager.SETTINGS_FILE_NAME);
		File settingsFile = file.getLocation().toFile();

		if (!settingsFile.exists()) {
			monitor.done();
			return;
		}

		// Backup the file
		monitor.beginTask(ConverterMessages.getFormattedString("UpdateManagedProject12.0", projectName), 1); //$NON-NLS-1$
		IManagedBuildInfo info = ManagedBuildManager.getBuildInfo(project);
		UpdateManagedProjectManager.backupFile(file, "_12backup", monitor, project); //$NON-NLS-1$

		IManagedProject newProject = null;

		//Now convert each target to the new format
		try {
			// Load the old build file
			InputStream stream = new FileInputStream(settingsFile);
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = parser.parse(stream);

			// Clone the target based on the proper target definition
			NodeList targetNodes = document.getElementsByTagName(ITarget.TARGET_ELEMENT_NAME);
			// This is a guess, but typically the project has 1 target, 2 configs, and 6 tool defs
			int listSize = targetNodes.getLength();
			monitor.beginTask(ConverterMessages.getFormattedString("UpdateManagedProject12.1", projectName), //$NON-NLS-1$
					listSize * 9);
			for (int targIndex = 0; targIndex < listSize; ++targIndex) {
				Element oldTarget = (Element) targetNodes.item(targIndex);
				String oldTargetId = oldTarget.getAttribute(ITarget.ID);
				newProject = convertTarget(project, oldTarget, monitor);
				// Remove the old target
				if (newProject != null) {
					info.removeTarget(oldTargetId);
					monitor.worked(1);
				}
			}

			// Set the default configuration
			NodeList defaultConfiguration = document.getElementsByTagName(IManagedBuildInfo.DEFAULT_CONFIGURATION);
			try {
				Element defaultConfig = (Element) defaultConfiguration.item(0);
				String oldDefaultConfigId = defaultConfig.getAttribute(IBuildObject.ID);
				IConfiguration newDefaultConfig = getConfigIdMap().get(oldDefaultConfigId);
				if (newDefaultConfig != null) {
					info.setDefaultConfiguration(newDefaultConfig);
					info.setSelectedConfiguration(newDefaultConfig);
				} else {
					// The only safe thing to do if there wasn't a default configuration for a built-in
					// target is to set the first defined configuration as the default
					IConfiguration[] newConfigs = newProject.getConfigurations();
					if (newConfigs.length > 0) {
						info.setDefaultConfiguration(newConfigs[0]);
						info.setSelectedConfiguration(newConfigs[0]);
					}
				}
			} catch (IndexOutOfBoundsException e) {
				throw new CoreException(new Status(IStatus.ERROR, ManagedBuilderCorePlugin.getUniqueIdentifier(), -1,
						ConverterMessages.getFormattedString("UpdateManagedProject12.7", newProject.getName()), null)); //$NON-NLS-1$
			}

			// Upgrade the version
			((ManagedBuildInfo) info).setVersion("2.1.0"); //$NON-NLS-1$
			info.setValid(true);
		} catch (CoreException e) {
			throw e;
		} catch (Exception e) {
			throw new CoreException(
					new Status(IStatus.ERROR, ManagedBuilderCorePlugin.getUniqueIdentifier(), -1, e.getMessage(), e));
		} finally {
			// If the tree is locked spawn a job to this.
			IWorkspace workspace = project.getWorkspace();
			//			boolean treeLock = workspace.isTreeLocked();
			ISchedulingRule rule = workspace.getRuleFactory().createRule(project);
			//since the java synchronized mechanism is now used for the build info loadding,
			//initiate the job in all cases
			//			if (treeLock) {
			WorkspaceJob job = new WorkspaceJob(ConverterMessages.getResourceString("UpdateManagedProject.notice")) { //$NON-NLS-1$
				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
					ManagedBuildManager.saveBuildInfoLegacy(project, false);
					return Status.OK_STATUS;
				}
			};
			job.setRule(rule);
			job.schedule();
			//			} else {
			//				ManagedBuildManager.saveBuildInfo(project, false);
			//			}
			monitor.done();
		}
	}