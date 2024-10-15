	public static boolean manages(IResource resource) {
		ICProjectDescription des = CoreModel.getDefault().getProjectDescription(resource.getProject(), false);
		if (des == null) {
			return false;
		}

		ICConfigurationDescription cfgDes = des.getActiveConfiguration();
		IConfiguration cfg = ManagedBuildManager.getConfigurationForDescription(cfgDes);
		if (cfg != null)
			return true;
		return false;

		//		// The managed build manager manages build information for the
		//		// resource IFF it it is a project and has a build file with the proper
		//		// root element
		//		IProject project = null;
		//		if (resource instanceof IProject){
		//			project = (IProject)resource;
		//		} else if (resource instanceof IFile) {
		//			project = ((IFile)resource).getProject();
		//		} else {
		//			return false;
		//		}
		//		IFile file = project.getFile(SETTINGS_FILE_NAME);
		//		if (file.exists()) {
		//			try {
		//				InputStream stream = file.getContents();
		//				DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		//				Document document = parser.parse(stream);
		//				NodeList nodes = document.getElementsByTagName(ROOT_NODE_NAME);
		//				return (nodes.getLength() > 0);
		//			} catch (Exception e) {
		//				return false;
		//			}
		//		}
		//		return false;
	}