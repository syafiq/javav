	private static ManagedBuildInfo loadOldStyleBuildInfo(final IProject project) throws Exception {
		ManagedBuildInfo buildInfo = null;
		IFile file = project.getFile(SETTINGS_FILE_NAME);
		File cdtbuild = file.getLocation().toFile();
		if (!cdtbuild.exists()) {
			// If we cannot find the .cdtbuild project file, throw an exception and let the user know
			throw new BuildException(ManagedMakeMessages.getFormattedString(PROJECT_FILE_ERROR, project.getName()));
		}

		// So there is a project file, load the information there
		try (InputStream stream = new FileInputStream(cdtbuild)) {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = parser.parse(stream);
			String fileVersion = null;

			// Get the first element in the project file
			Node rootElement = document.getFirstChild();

			// Since 2.0 this will be a processing instruction containing version
			if (rootElement.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE) {
				// This is a 1.2 project and it must be updated
			} else {
				// Make sure that the version is compatible with the manager
				fileVersion = rootElement.getNodeValue();
				Version version = new Version(fileVersion);
				//if buildInfoVersion is greater than fileVersion
				if (buildInfoVersion.compareTo(version) > 0) {
					// This is >= 2.0 project, but earlier than the current MBS version - it may need to be updated
				} else {
					// This is a
					//  isCompatibleWith will return FALSE, if:
					//   o  The major versions are not equal
					//   o  The major versions are equal, but the remainder of the .cdtbuild version # is
					//      greater than the MBS version #
					boolean compatible = false;
					if (buildInfoVersion.getMajor() != version.getMajor())
						compatible = false;
					if (buildInfoVersion.getMinor() > version.getMinor())
						compatible = true;
					if (buildInfoVersion.getMinor() < version.getMinor())
						compatible = false;
					if (buildInfoVersion.getMicro() > version.getMicro())
						compatible = true;
					if (buildInfoVersion.getMicro() < version.getMicro())
						compatible = false;
					if (buildInfoVersion.getQualifier().compareTo(version.getQualifier()) >= 0)
						compatible = true;
					if (!compatible) {
						throw new BuildException(
								ManagedMakeMessages.getFormattedString(PROJECT_VERSION_ERROR, project.getName()));
					}
				}
			}

			// Now get the project root element (there should be only one)
			NodeList nodes = document.getElementsByTagName(ROOT_NODE_NAME);
			if (nodes.getLength() > 0) {
				Node node = nodes.item(0);

				//  Create the internal representation of the project's MBS information
				buildInfo = new ManagedBuildInfo(project, XmlStorageUtil.createCStorageTree((Element) node), true,
						fileVersion);
				if (fileVersion != null) {
					//				buildInfo.setVersion(fileVersion);
					Version version = new Version(fileVersion);
					Version version21 = new Version("2.1"); //$NON-NLS-1$
					//  CDT 2.1 is the first version using the new MBS model
					if (version.compareTo(version21) >= 0) {
						//  Check to see if all elements could be loaded correctly - for example,
						//  if references in the project file could not be resolved to extension
						//  elements
						if (buildInfo.getManagedProject() == null || (!buildInfo.getManagedProject().isValid())) {
							//  The load failed
							throw new Exception(ManagedMakeMessages
									.getFormattedString("ManagedBuildManager.error.id.nomatch", project.getName())); //$NON-NLS-1$
						}

						// Each ToolChain/Tool/Builder element maintain two separate
						// converters if available
						// 0ne for previous Mbs versions and one for current Mbs version
						// walk through the project hierarchy and call the converters
						// written for previous mbs versions
						if (checkForMigrationSupport(buildInfo, false) != true) {
							// display an error message that the project is not loadable
							if (buildInfo.getManagedProject() == null || (!buildInfo.getManagedProject().isValid())) {
								//  The load failed
								throw new Exception(ManagedMakeMessages
										.getFormattedString("ManagedBuildManager.error.id.nomatch", project.getName())); //$NON-NLS-1$
							}
						}
					}
				}

				//  Upgrade the project's CDT version if necessary
				if (!UpdateManagedProjectManager.isCompatibleProject(buildInfo)) {
					UpdateManagedProjectManager.updateProject(project, buildInfo);
				}
				//  Check to see if the upgrade (if required) succeeded
				if (buildInfo.getManagedProject() == null || (!buildInfo.getManagedProject().isValid())) {
					//  The load failed
					throw new Exception(ManagedMakeMessages.getFormattedString("ManagedBuildManager.error.id.nomatch", //$NON-NLS-1$
							project.getName()));
				}

				//  Walk through the project hierarchy and call the converters
				//  written for current mbs version
				if (checkForMigrationSupport(buildInfo, true) != true) {
					// display an error message.that the project is no loadable
					if (buildInfo.getManagedProject() == null || (!buildInfo.getManagedProject().isValid())) {
						//  The load failed
						throw new Exception(ManagedMakeMessages
								.getFormattedString("ManagedBuildManager.error.id.nomatch", project.getName())); //$NON-NLS-1$
					}
				}

				IConfiguration[] configs = buildInfo.getManagedProject().getConfigurations();
				//  Send an event to each configuration and if they exist, its resource configurations
				for (IConfiguration cfg : configs) {
					ManagedBuildManager.performValueHandlerEvent(cfg, IManagedOptionValueHandler.EVENT_OPEN);
				}
				//  Finish up
				//project.setSessionProperty(buildInfoProperty, buildInfo);
				setLoaddedBuildInfo(project, buildInfo);
			}
		}

		if (buildInfo != null) {
			buildInfo.setValid(true);
		}
		return buildInfo;
	}