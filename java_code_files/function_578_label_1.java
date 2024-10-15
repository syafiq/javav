	public void loadVariablesAndContainers() throws CoreException {
		// backward compatibility, consider persistent property
		QualifiedName qName = new QualifiedName(JavaCore.PLUGIN_ID, "variables"); //$NON-NLS-1$
		String xmlString = ResourcesPlugin.getWorkspace().getRoot().getPersistentProperty(qName);

		try {
			if (xmlString != null){
				StringReader reader = new StringReader(xmlString);
				Element cpElement;
				try {
					DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					cpElement = parser.parse(new InputSource(reader)).getDocumentElement();
				} catch(SAXException | ParserConfigurationException e){
					return;
				} finally {
					reader.close();
				}
				if (cpElement == null) return;
				if (!cpElement.getNodeName().equalsIgnoreCase("variables")) { //$NON-NLS-1$
					return;
				}

				NodeList list= cpElement.getChildNodes();
				int length= list.getLength();
				for (int i= 0; i < length; ++i) {
					Node node= list.item(i);
					short type= node.getNodeType();
					if (type == Node.ELEMENT_NODE) {
						Element element= (Element) node;
						if (element.getNodeName().equalsIgnoreCase("variable")) { //$NON-NLS-1$
							variablePut(
								element.getAttribute("name"), //$NON-NLS-1$
								new Path(element.getAttribute("path"))); //$NON-NLS-1$
						}
					}
				}
			}
		} catch(IOException e){
			// problem loading xml file: nothing we can do
		} finally {
			if (xmlString != null){
				ResourcesPlugin.getWorkspace().getRoot().setPersistentProperty(qName, null); // flush old one
			}
		}

		// backward compatibility, load variables and containers from preferences into cache
		loadVariablesAndContainers(getDefaultPreferences());
		loadVariablesAndContainers(getInstancePreferences());

		// load variables and containers from saved file into cache
		File file = getVariableAndContainersFile();
		DataInputStream in = null;
		try {
			in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			switch (in.readInt()) {
				case 2 :
					new VariablesAndContainersLoadHelper(in).load();
					break;
				case 1 : // backward compatibility, load old format
					// variables
					int size = in.readInt();
					while (size-- > 0) {
						String varName = in.readUTF();
						String pathString = in.readUTF();
						if (CP_ENTRY_IGNORE.equals(pathString))
							continue;
						IPath varPath = Path.fromPortableString(pathString);
						this.variables.put(varName, varPath);
						this.previousSessionVariables.put(varName, varPath);
					}

					// containers
					IJavaModel model = getJavaModel();
					int projectSize = in.readInt();
					while (projectSize-- > 0) {
						String projectName = in.readUTF();
						IJavaProject project = model.getJavaProject(projectName);
						int containerSize = in.readInt();
						while (containerSize-- > 0) {
							IPath containerPath = Path.fromPortableString(in.readUTF());
							int length = in.readInt();
							byte[] containerString = new byte[length];
							in.readFully(containerString);
							recreatePersistedContainer(project, containerPath, new String(containerString), true/*add to container values*/);
						}
					}
					break;
			}
		} catch (IOException e) {
			if (file.exists())
				Util.log(e, "Unable to read variable and containers file"); //$NON-NLS-1$
		} catch (RuntimeException e) {
			if (file.exists())
				Util.log(e, "Unable to read variable and containers file (file is corrupt)"); //$NON-NLS-1$
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// nothing we can do: ignore
				}
			}
		}

		// override persisted values for variables which have a registered initializer
		String[] registeredVariables = getRegisteredVariableNames();
		for (int i = 0; i < registeredVariables.length; i++) {
			String varName = registeredVariables[i];
			this.variables.put(varName, null); // reset variable, but leave its entry in the Map, so it will be part of variable names.
		}
		// override persisted values for containers which have a registered initializer
		containersReset(getRegisteredContainerIDs());
	}