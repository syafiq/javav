	public IClasspathEntry[][] decodeClasspath(String xmlClasspath, Map unknownElements) throws IOException, ClasspathEntry.AssertionFailedException {

		ArrayList paths = new ArrayList();
		IClasspathEntry defaultOutput = null;
		StringReader reader = new StringReader(xmlClasspath);
		Element cpElement;
		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			cpElement = parser.parse(new InputSource(reader)).getDocumentElement();
		} catch (SAXException | ParserConfigurationException e) {
			throw new IOException(Messages.file_badFormat, e);
		} finally {
			reader.close();
		}

		if (!cpElement.getNodeName().equalsIgnoreCase("classpath")) { //$NON-NLS-1$
			throw new IOException(Messages.file_badFormat);
		}
		NodeList list = cpElement.getElementsByTagName(ClasspathEntry.TAG_CLASSPATHENTRY);
		int length = list.getLength();

		for (int i = 0; i < length; ++i) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				IClasspathEntry entry = ClasspathEntry.elementDecode((Element)node, this, unknownElements);
				if (entry != null){
					if (entry.getContentKind() == ClasspathEntry.K_OUTPUT) {
						defaultOutput = entry; // separate output
					} else {
						paths.add(entry);
					}
				}
			}
		}
		int pathSize = paths.size();
		IClasspathEntry[][] entries = new IClasspathEntry[2][];
		entries[0] = new IClasspathEntry[pathSize + (defaultOutput == null ? 0 : 1)];
		paths.toArray(entries[0]);
		if (defaultOutput != null) entries[0][pathSize] = defaultOutput; // ensure output is last item

		paths.clear();
		list = cpElement.getElementsByTagName(ClasspathEntry.TAG_REFERENCED_ENTRY);
		length = list.getLength();

		for (int i = 0; i < length; ++i) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				IClasspathEntry entry = ClasspathEntry.elementDecode((Element)node, this, unknownElements);
				if (entry != null){
					paths.add(entry);
				}
			}
		}
		entries[1] = new IClasspathEntry[paths.size()];
		paths.toArray(entries[1]);

		return entries;
	}