		private static List<CPUserLibraryElement> loadLibraries(File file) throws IOException {
			InputStream stream= new FileInputStream(file);
			Element cpElement;
			try {
				DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				parser.setErrorHandler(new DefaultHandler());
				cpElement = parser.parse(new InputSource(stream)).getDocumentElement();
			} catch (SAXException | ParserConfigurationException e) {
				throw new IOException(PreferencesMessages.UserLibraryPreferencePage_LoadSaveDialog_load_badformat);
			} finally {
				stream.close();
			}

			if (!TAG_ROOT.equalsIgnoreCase(cpElement.getNodeName())) {
				throw new IOException(PreferencesMessages.UserLibraryPreferencePage_LoadSaveDialog_load_badformat);
			}

			String version= cpElement.getAttribute(TAG_VERSION);

			NodeList libList= cpElement.getElementsByTagName(TAG_LIBRARY);
			int length = libList.getLength();

			IWorkspaceRoot root= ResourcesPlugin.getWorkspace().getRoot();

			ArrayList<CPUserLibraryElement> result= new ArrayList<>(length);
			for (int i= 0; i < length; i++) {
				Node lib= libList.item(i);
				if (!(lib instanceof Element)) {
					continue;
				}
				Element libElement= (Element) lib;
				String name= libElement.getAttribute(TAG_NAME);
				boolean isSystem= Boolean.parseBoolean(libElement.getAttribute(TAG_SYSTEMLIBRARY));

				CPUserLibraryElement newLibrary= new CPUserLibraryElement(name, isSystem, null);
				result.add(newLibrary);

				NodeList archiveList= libElement.getElementsByTagName(TAG_ARCHIVE);
				for (int k= 0; k < archiveList.getLength(); k++) {
					Node archiveNode= archiveList.item(k);
					if (!(archiveNode instanceof Element)) {
						continue;
					}
					Element archiveElement= (Element) archiveNode;

					String pathString= archiveElement.getAttribute(TAG_ARCHIVE_PATH);
					IPath path= VERSION1.equals(version) ? Path.fromOSString(pathString) : Path.fromPortableString(pathString);
					path= path.makeAbsolute(); // only necessary for manually edited files: bug 202373

					IResource resource= root.findMember(path); // support internal JARs: bug 133191
					if (!(resource instanceof IFile)) {
						resource= null;
					}

					CPListElement newArchive= new CPListElement(newLibrary, null, IClasspathEntry.CPE_LIBRARY, path, resource);
					newLibrary.add(newArchive);

					if (archiveElement.hasAttribute(TAG_SOURCEATTACHMENT)) {
						String sourceAttachString= archiveElement.getAttribute(TAG_SOURCEATTACHMENT);
						IPath sourceAttach= VERSION1.equals(version) ? Path.fromOSString(sourceAttachString) : Path.fromPortableString(sourceAttachString);
						newArchive.setAttribute(CPListElement.SOURCEATTACHMENT, sourceAttach);
					}
					if (archiveElement.hasAttribute(TAG_SOURCE_ATTACHMENT_ENCODING)) {
						String javadoc= archiveElement.getAttribute(TAG_SOURCE_ATTACHMENT_ENCODING);
						newArchive.setAttribute(CPListElement.SOURCE_ATTACHMENT_ENCODING, javadoc);
					}
					if (archiveElement.hasAttribute(TAG_JAVADOC)) {
						String javadoc= archiveElement.getAttribute(TAG_JAVADOC);
						newArchive.setAttribute(CPListElement.JAVADOC, javadoc);
					}
					if (archiveElement.hasAttribute(TAG_NATIVELIB_PATHS)) {
						String nativeLibPath= archiveElement.getAttribute(TAG_NATIVELIB_PATHS);
						newArchive.setAttribute(CPListElement.NATIVE_LIB_PATH, nativeLibPath);
					}
					NodeList rulesParentNodes= archiveElement.getElementsByTagName(TAG_ACCESSRULES);
					if (rulesParentNodes.getLength() > 0 && rulesParentNodes.item(0) instanceof Element) {
						Element ruleParentElement= (Element) rulesParentNodes.item(0); // take first, ignore others
						NodeList ruleElements= ruleParentElement.getElementsByTagName(TAG_ACCESSRULE);
						int nRuleElements= ruleElements.getLength();
						if (nRuleElements > 0) {
							ArrayList<IAccessRule> resultingRules= new ArrayList<>(nRuleElements);
							for (int n= 0; n < nRuleElements; n++) {
								Node node= ruleElements.item(n);
								if (node instanceof Element) {
									Element ruleElement= (Element) node;
									try {
										int kind= Integer.parseInt(ruleElement.getAttribute(TAG_RULE_KIND));
										IPath pattern= Path.fromPortableString(ruleElement.getAttribute(TAG_RULE_PATTERN));
										resultingRules.add(JavaCore.newAccessRule(pattern, kind));
									} catch (NumberFormatException e) {
										// ignore
									}
								}
							}
							newArchive.setAttribute(CPListElement.ACCESSRULES, resultingRules.toArray(new IAccessRule[resultingRules.size()]));
						}
					}
				}
			}
			return result;
		}