	protected static Document getACDoc(String acDocVer) {
		Document acDocument = null;
		if (acHoverDocs == null) {
			acHoverDocs = new HashMap<>();
		}
		acDocument = acHoverDocs.get(acDocVer);
		if (acDocument == null) {
			Document doc = null;
			try {
				// see comment in initialize()
				try {
					InputStream docStream = null;
					try {
						URI uri = new URI(getLocalAutoconfMacrosDocName(acDocVer));
						IPath p = URIUtil.toPath(uri);
						// Try to open the file as local to this plug-in.
						docStream = FileLocator.openStream(AutotoolsUIPlugin.getDefault().getBundle(), p, false);
					} catch (IOException e) {
						// Local open failed.  Try normal external location.
						URI acDoc = new URI(getAutoconfMacrosDocName(acDocVer));
						IPath p = URIUtil.toPath(acDoc);
						if (p == null) {
							URL url = acDoc.toURL();
							docStream = url.openStream();
						} else {
							docStream = new FileInputStream(p.toFile());
						}
					}
					DocumentBuilderFactory factory = XmlProcessorFactoryCdt
							.createDocumentBuilderFactoryIgnoringDOCTYPE();
					factory.setValidating(false);
					try {
						DocumentBuilder builder = factory.newDocumentBuilder();
						doc = builder.parse(docStream);
					} catch (SAXException | ParserConfigurationException | IOException saxEx) {
						AutotoolsPlugin.log(saxEx);
						doc = null;
					} finally {
						if (docStream != null)
							docStream.close();
					}
				} catch (FileNotFoundException | MalformedURLException | URISyntaxException e) {
					AutotoolsPlugin.log(e);
				}
				acDocument = doc;
			} catch (IOException ioe) {
			}
		}
		acHoverDocs.put(acDocVer, acDocument);
		return acDocument;
	}