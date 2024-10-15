	private static Element readXML(IPath xmlFilePath) throws Exception {
		try (InputStream in = new FileInputStream(xmlFilePath.toFile())) {
			DocumentBuilder parser= DocumentBuilderFactory.newInstance().newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			Element root= parser.parse(new InputSource(in)).getDocumentElement();
			in.close();

			return root;
		}
	}