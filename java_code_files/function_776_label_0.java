	public void t1canSetConfigParm() throws Exception {
		IProject project = checkProject();
		assertNotNull(project);
		IPath path = project.getLocation();
		path = path.append(".autotools");
		File f = new File(path.toOSString());
		assertTrue(f.exists());

		DocumentBuilder db = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
		Document d = db.parse(f);
		Element e = d.getDocumentElement();
		// Get the stored configuration data
		NodeList cfgs = e.getElementsByTagName("configuration"); //$NON-NLS-1$
		assertTrue(cfgs.getLength() > 0);
		Node n = cfgs.item(0);
		NodeList l = n.getChildNodes();
		// Verify user field in .autotools file is set to --enable-jeff
		boolean foundUser = false;
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
					foundUser = true;
					assertEquals(value, "--enable-jeff");
				}
			}
		}
		assertTrue(foundUser);
	}