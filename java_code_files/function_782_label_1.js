	protected static Document getAMDoc(String amDocVer) {
		Document amDocument = null;
		if (amHoverDocs == null) {
			amHoverDocs = new HashMap<>();
		}
		amDocument = amHoverDocs.get(amDocVer);
		if (amDocument == null) {
			Document doc = null;
			try {
				// see comment in initialize()
				try {
					InputStream docStream = null;
					try {
						URI uri = new URI(getLocalAutomakeMacrosDocName(amDocVer));
						IPath p = URIUtil.toPath(uri);
						// Try to open the file as local to this plug-in.
						docStream = FileLocator.openStream(AutotoolsUIPlugin.getDefault().getBundle(), p, false);
					} catch (IOException e) {
						// Local open failed.  Try normal external location.
						URI acDoc = new URI(getAutomakeMacrosDocName(amDocVer));
						IPath p = URIUtil.toPath(acDoc);
						if (p == null) {
							URL url = acDoc.toURL();
							docStream = url.openStream();
						} else {
							docStream = new FileInputStream(p.toFile());
						}
					}
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					factory.setValidating(false);
					try {
						DocumentBuilder builder = factory.newDocumentBuilder();
						doc = builder.parse(docStream);
					} catch (SAXException | ParserConfigurationException | IOException ex) {
						doc = null;
					} finally {
						if (docStream != null)
							docStream.close();
					}
				} catch (FileNotFoundException | MalformedURLException | URISyntaxException e) {
					AutotoolsPlugin.log(e);
				}
				amDocument = doc;
			} catch (IOException ioe) {
			}
		}
		amHoverDocs.put(amDocVer, amDocument);
		return amDocument;
	}