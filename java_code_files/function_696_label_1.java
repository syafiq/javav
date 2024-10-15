	private void loadFile(IConfigurationElement el, ArrayList<ICHelpBook> chbl, String pluginId) {
		String fname = el.getAttribute(ATTRIB_FILE);
		if (fname == null || fname.trim().length() == 0)
			return;
		URL x = FileLocator.find(Platform.getBundle(pluginId), new Path(fname), null);
		if (x == null)
			return;
		try {
			x = FileLocator.toFileURL(x);
		} catch (IOException e) {
			return;
		}
		fname = x.getPath();
		if (fname == null || fname.trim().length() == 0)
			return;

		// format is not supported for now
		// String format = el.getAttribute(ATTRIB_FORMAT);

		Document doc = null;
		try {
			InputStream stream = new FileInputStream(fname);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			InputSource src = new InputSource(reader);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = builder.parse(src);
			Element e = doc.getDocumentElement();
			if (NODE_HEAD.equals(e.getNodeName())) {
				NodeList list = e.getChildNodes();
				for (int j = 0; j < list.getLength(); j++) {
					Node node = list.item(j);
					if (node.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if (NODE_BOOK.equals(node.getNodeName())) {
						chbl.add(new CHelpBook((Element) node));
					}
				}
			}
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		}
	}