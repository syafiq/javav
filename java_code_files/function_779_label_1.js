	public void t3newConfigCopiesParms() throws Exception {
		projectExplorer.bot().tree().select(projectName);
		clickContextMenu(projectExplorer.bot().tree().select(projectName), "Build Configurations", "Manage...");
		SWTBotShell shell = bot.shell(projectName + ": Manage Configurations");
		shell.activate();
		shell = bot.shell(projectName + ": Manage Configurations");
		shell.activate();
		SWTBotTable table = bot.table();
		assertTrue(table.containsItem("debug"));
		table.getTableItem("debug").select();
		bot.button("Set Active").click();
		bot.button("OK").click();
		// Verify the debug configuration is active and has a user parameter:
		// --enable-jeff
		openProperties("Autotools", "Configure Settings");
		SWTBotCombo configs = bot.comboBoxWithLabel("Configuration: ");
		configs.setFocus();
		String[] items = configs.items();
		for (int i = 0; i < items.length; ++i) {
			if (items[i].contains("debug") && items[i].contains("Active")) {
				configs.setSelection(i);
			}
		}
		assertTrue(configs.getText().contains("debug"));
		bot.treeWithLabel("Configure Settings").expandNode("configure").select("Advanced");
		SWTBotText text = bot.textWithLabel("Additional command-line options");
		String val = text.getText();
		assertEquals("--enable-jeff", val);
		// Verify that the build directory for the new configuration has been
		// switched to build-debug
		shell = bot.shell("Properties for " + projectName);
		shell.activate();
		bot.text().setText("");

		bot.tree().select("C/C++ Build");
		String buildDir = bot.textWithLabel("Build directory:").getText();
		assertTrue(buildDir.endsWith("build-debug"));
		// Verify the default configuration has no user setting
		bot.tree().expandNode("Autotools").select("Configure Settings");
		configs = bot.comboBoxWithLabel("Configuration: ");
		configs.setSelection("default");
		bot.treeWithLabel("Configure Settings").expandNode("configure").select("Advanced");
		text = bot.textWithLabel("Additional command-line options");
		val = text.getText();
		assertEquals("", val);
		bot.button("OK").click();
		// Build the project again and verify we get a build-debug directory
		projectExplorer.bot().tree().select(projectName);
		clickContextMenu(projectExplorer.bot().tree().select(projectName), "Build Project");
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		assertNotNull(workspace);
		IWorkspaceRoot root = workspace.getRoot();
		assertNotNull(root);
		IProject project = root.getProject(projectName);
		assertNotNull(project);
		IPath path = project.getLocation();
		// We need to wait until the config.status file is created so
		// sleep a bit and look for it...give up after 20 seconds
		File f = null;
		for (int i = 0; i < 40; ++i) {
			bot.sleep(500);
			f = new File(path.append("build-debug/src/a.out").toOSString());
			if (f.exists()) {
				break;
			}
		}
		assertTrue(f.exists());
		f = new File(path.append("build-debug/config.status").toOSString());
		assertTrue(f.exists());
		try (BufferedReader r = new BufferedReader(new FileReader(f))) {
			int ch;
			boolean optionFound = false;
			// Read config.status and look for the string --enable-jeff
			// which is a simple verification that the option was used in the
			// configure step.
			while ((ch = r.read()) != -1) {
				if (ch == '-') {
					char[] buf = new char[12];
					r.mark(100);
					int count = r.read(buf);
					if (count < 12) {
						break;
					}
					String s = new String(buf);
					if (s.equals("-enable-jeff")) {
						optionFound = true;
						break;
					} else {
						r.reset();
					}
				}
			}
			assertTrue(optionFound);
		}
		// Verify we cleaned out the top-level build directory (i.e. that there
		// is no config.status there anymore).
		path = project.getLocation().append("config.status");
		f = new File(path.toOSString());
		assertTrue(!f.exists());
		path = project.getLocation().append(".autotools");
		f = new File(path.toOSString());
		assertTrue(f.exists());
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document d = db.parse(f);
		Element e = d.getDocumentElement();
		// Get the stored configuration data
		NodeList cfgs = e.getElementsByTagName("configuration"); //$NON-NLS-1$
		assertEquals(4, cfgs.getLength());
		int foundUser = 0;
		for (int x = 0; x < cfgs.getLength(); ++x) {
			Node n = cfgs.item(x);
			NodeList l = n.getChildNodes();
			// Verify two of the user fields in .autotools file are set to
			// --enable-jeff
			for (int y = 0; y < l.getLength(); ++y) {
				Node child = l.item(y);
				if (child.getNodeName().equals("option")) { //$NON-NLS-1$
					NamedNodeMap optionAttrs = child.getAttributes();
					Node idNode = optionAttrs.getNamedItem("id"); //$NON-NLS-1$
					Node valueNode = optionAttrs.getNamedItem("value"); //$NON-NLS-1$
					assertNotNull(idNode);
					assertNotNull(valueNode);
					String id = idNode.getNodeValue();
					String value = valueNode.getNodeValue();
					if (id.equals("user")) {
						if (value.equals("--enable-jeff")) {
							++foundUser;
						}
					}
				}
			}
		}
		assertEquals(2, foundUser);

		clickContextMenu(projectExplorer.bot().tree().select(projectName), "Build Configurations", "Manage...");
		shell = bot.shell(projectName + ": Manage Configurations");
		shell.activate();
		table = bot.table();
		assertTrue(table.containsItem("Build (GNU)"));
		table.getTableItem("Build (GNU)").select();
		bot.button("Set Active").click();
		bot.button("OK").click();
		bot.waitUntil(Conditions.shellCloses(shell));
	}