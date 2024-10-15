	private void loadActionData() {

		String actionData = CDebugCorePlugin.getDefault().getPluginPreferences().getString(BREAKPOINT_ACTION_DATA);

		if (actionData == null || actionData.length() == 0)
			return;

		Element root = null;
		DocumentBuilder parser;
		try {
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			root = parser.parse(new InputSource(new StringReader(actionData))).getDocumentElement();

			NodeList nodeList = root.getChildNodes();
			int entryCount = nodeList.getLength();

			for (int i = 0; i < entryCount; i++) {
				Node node = nodeList.item(i);
				short type = node.getNodeType();
				if (type == Node.ELEMENT_NODE) {
					Element subElement = (Element) node;
					String nodeName = subElement.getNodeName();
					if (nodeName.equalsIgnoreCase("actionEntry")) { //$NON-NLS-1$
						String name = subElement.getAttribute("name"); //$NON-NLS-1$
						if (name == null)
							throw new Exception();
						String value = subElement.getAttribute("value"); //$NON-NLS-1$
						if (value == null)
							throw new Exception();
						String className = subElement.getAttribute("class"); //$NON-NLS-1$
						if (className == null)
							throw new Exception();

						IBreakpointAction action = createActionFromClassName(name, className);
						action.initializeFromMemento(value);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}