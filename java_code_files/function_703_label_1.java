	public void saveRecentSounds() {
		String recentSoundData = ""; //$NON-NLS-1$

		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = dfactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("recentSounds"); //$NON-NLS-1$
			doc.appendChild(rootElement);

			for (Iterator<File> iter = recentSounds.iterator(); iter.hasNext();) {
				File soundFile = iter.next();

				Element element = doc.createElement("soundFileName"); //$NON-NLS-1$
				element.setAttribute("name", soundFile.getAbsolutePath()); //$NON-NLS-1$
				rootElement.appendChild(element);

			}

			ByteArrayOutputStream s = new ByteArrayOutputStream();

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

			DOMSource source = new DOMSource(doc);
			StreamResult outputTarget = new StreamResult(s);
			transformer.transform(source, outputTarget);

			recentSoundData = s.toString("UTF8"); //$NON-NLS-1$

		} catch (Exception e) {
			e.printStackTrace();
		}

		CDebugUIPlugin.getDefault().getPreferenceStore().setValue(SOUND_ACTION_RECENT, recentSoundData);
	}