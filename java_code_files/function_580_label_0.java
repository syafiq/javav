	public static UserLibrary createFromString(Reader reader) throws IOException {
		Element cpElement;
		try {
			@SuppressWarnings("restriction")
			DocumentBuilder parser = org.eclipse.core.internal.runtime.XmlProcessorFactory.createDocumentBuilderWithErrorOnDOCTYPE();
			cpElement = parser.parse(new InputSource(reader)).getDocumentElement();
		} catch (SAXException | ParserConfigurationException e) {
			throw new IOException(Messages.file_badFormat, e);
		} finally {
			reader.close();
		}

		if (!cpElement.getNodeName().equalsIgnoreCase(TAG_USERLIBRARY)) {
			throw new IOException(Messages.file_badFormat);
		}
		String version= cpElement.getAttribute(TAG_VERSION);
		boolean isSystem= Boolean.parseBoolean(cpElement.getAttribute(TAG_SYSTEMLIBRARY));

		NodeList list= cpElement.getChildNodes();
		int length = list.getLength();

		ArrayList res= new ArrayList(length);
		for (int i = 0; i < length; ++i) {
			Node node = list.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element= (Element) node;
				if (element.getNodeName().equals(TAG_ARCHIVE)) {
					String pathString = element.getAttribute(TAG_PATH);
					String sourceAttachString = element.hasAttribute(TAG_SOURCEATTACHMENT) ? element.getAttribute(TAG_SOURCEATTACHMENT) : null;
					String sourceAttachRootString = element.hasAttribute(TAG_SOURCEATTACHMENTROOT) ? element.getAttribute(TAG_SOURCEATTACHMENTROOT) : null;
					IPath entryPath = null;
					IPath sourceAttachPath = null;
					IPath sourceAttachRootPath = null;
					if (version.equals(VERSION_ONE)) {
						entryPath = Path.fromOSString(pathString);
						if (sourceAttachString != null) sourceAttachPath = Path.fromOSString(sourceAttachString);
						if (sourceAttachRootString != null) sourceAttachRootPath = Path.fromOSString(sourceAttachRootString);
					}
					else {
						entryPath = Path.fromPortableString(pathString);
						if (sourceAttachString != null) sourceAttachPath = Path.fromPortableString(sourceAttachString);
						if (sourceAttachRootString != null) sourceAttachRootPath = Path.fromPortableString(sourceAttachRootString);
					}

					NodeList children = element.getElementsByTagName("*"); //$NON-NLS-1$
					boolean[] foundChildren = new boolean[children.getLength()];
					NodeList attributeList = ClasspathEntry.getChildAttributes(ClasspathEntry.TAG_ATTRIBUTES, children, foundChildren);
					IClasspathAttribute[] extraAttributes = ClasspathEntry.decodeExtraAttributes(attributeList);
					attributeList = ClasspathEntry.getChildAttributes(ClasspathEntry.TAG_ACCESS_RULES, children, foundChildren);
					IAccessRule[] accessRules = ClasspathEntry.decodeAccessRules(attributeList);
					IClasspathEntry entry = JavaCore.newLibraryEntry(entryPath, sourceAttachPath, sourceAttachRootPath, accessRules, extraAttributes, false/*not exported*/);
					res.add(entry);
				}
			}
		}

		IClasspathEntry[] entries= (IClasspathEntry[]) res.toArray(new IClasspathEntry[res.size()]);

		return new UserLibrary(entries, isSystem);
	}