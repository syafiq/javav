	private void loadRecentSounds() {
		String recentSoundData = CDebugUIPlugin.getDefault().getPreferenceStore().getString(SOUND_ACTION_RECENT);

		if (recentSoundData == null || recentSoundData.length() == 0) {
			initializeRecentSounds();
			return;
		}

		recentSounds = new ArrayList<>();

		Element root = null;
		DocumentBuilder parser;
		try {
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			root = parser.parse(new InputSource(new StringReader(recentSoundData))).getDocumentElement();

			NodeList nodeList = root.getChildNodes();
			int entryCount = nodeList.getLength();

			for (int i = 0; i < entryCount; i++) {
				Node node = nodeList.item(i);
				short type = node.getNodeType();
				if (type == Node.ELEMENT_NODE) {
					Element subElement = (Element) node;
					String nodeName = subElement.getNodeName();
					if (nodeName.equalsIgnoreCase("soundFileName")) { //$NON-NLS-1$
						String value = subElement.getAttribute("name"); //$NON-NLS-1$
						if (value == null)
							throw new Exception();

						File soundFile = new File(value);
						if (soundFile.exists()) {
							recentSounds.add(soundFile);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (recentSounds.size() == 0)
			initializeRecentSounds();
	}