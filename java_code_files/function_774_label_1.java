	private Map<String, IAConfiguration> getSavedConfigs(IProject project) {
		String projectName = project.getName();
		Map<String, IAConfiguration> list = configs.get(projectName);
		if (list == null) {
			try {
				IPath fileLocation = project.getLocation().append(CFG_FILE_NAME);
				File dirFile = fileLocation.toFile();
				Map<String, IAConfiguration> cfgList = new HashMap<>();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				if (dirFile.exists()) {
					Document d = db.parse(dirFile);
					Element e = d.getDocumentElement();
					// Get the stored configuration data
					NodeList cfgs = e.getElementsByTagName("configuration"); //$NON-NLS-1$
					for (int x = 0; x < cfgs.getLength(); ++x) {
						Node n = cfgs.item(x);
						NamedNodeMap attrs = n.getAttributes();
						// Originally we used the configuration name, but now we use
						// the ConfigurationDescription id which is unique.  Check for
						// id first, but fall back to name for older .autotools files.
						Node nameNode = attrs.getNamedItem("name"); //$NON-NLS-1$
						Node cfgIdNode = attrs.getNamedItem("id"); //$NON-NLS-1$
						String cfgId = null;
						if (cfgIdNode != null)
							cfgId = cfgIdNode.getNodeValue();
						else if (nameNode != null) {
							String cfgName = nameNode.getNodeValue();
							ICConfigurationDescription cfgd = CoreModel.getDefault().getProjectDescription(project)
									.getConfigurationByName(cfgName);
							if (cfgd != null)
								cfgId = cfgd.getId();
							else
								continue; // have to punt, this doesn't map to real cfg
						}
						IAConfiguration cfg = new AutotoolsConfiguration(project, cfgId);
						NodeList l = n.getChildNodes();
						for (int y = 0; y < l.getLength(); ++y) {
							Node child = l.item(y);
							if (child.getNodeName().equals("option")) { //$NON-NLS-1$
								NamedNodeMap optionAttrs = child.getAttributes();
								Node id = optionAttrs.getNamedItem("id"); //$NON-NLS-1$
								Node value = optionAttrs.getNamedItem("value"); //$NON-NLS-1$
								if (id != null && value != null)
									cfg.setOption(id.getNodeValue(), value.getNodeValue());
							} else if (child.getNodeName().equals("flag")) { //$NON-NLS-1$
								// read in flag values
								NamedNodeMap optionAttrs = child.getAttributes();
								Node id = optionAttrs.getNamedItem("id"); //$NON-NLS-1$
								String idValue = id.getNodeValue();
								IConfigureOption opt = cfg.getOption(idValue);
								if (opt instanceof FlagConfigureOption) {
									NodeList l2 = child.getChildNodes();
									for (int z = 0; z < l2.getLength(); ++z) {
										Node flagChild = l2.item(z);
										if (flagChild.getNodeName().equals("flagvalue")) { //$NON-NLS-1$
											NamedNodeMap optionAttrs2 = flagChild.getAttributes();
											Node id2 = optionAttrs2.getNamedItem("id"); //$NON-NLS-1$
											Node value = optionAttrs2.getNamedItem("value"); //$NON-NLS-1$
											cfg.setOption(id2.getNodeValue(), value.getNodeValue());
										}
									}
								}
							}
						}
						cfg.setDirty(false);
						cfgList.put(cfg.getId(), cfg);
					}
					if (cfgList.size() > 0) {
						configs.put(projectName, cfgList);
						list = cfgList;
					}
				}
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}