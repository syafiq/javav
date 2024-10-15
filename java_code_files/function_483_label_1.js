	public JarPackageData readXML(JarPackageData jarPackage) throws IOException, SAXException {
	  	DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
    	factory.setValidating(false);
		DocumentBuilder parser= null;

		try {
			parser= factory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			throw new IOException(ex.getLocalizedMessage());
		} finally {
			// Note: Above code is OK since clients are responsible to close the stream
		}
		parser.setErrorHandler(new DefaultHandler());
		Element xmlJarDesc= parser.parse(new InputSource(fInputStream)).getDocumentElement();
		if (!JarPackagerUtil.DESCRIPTION_EXTENSION.equals(xmlJarDesc.getNodeName())) {
			throw new IOException(JarPackagerMessages.JarPackageReader_error_badFormat);
		}
		NodeList topLevelElements= xmlJarDesc.getChildNodes();
		for (int i= 0; i < topLevelElements.getLength(); i++) {
			Node node= topLevelElements.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			Element element= (Element)node;
			xmlReadJarLocation(jarPackage, element);
			xmlReadOptions(jarPackage, element);
			xmlReadRefactoring(jarPackage, element);
			xmlReadSelectedProjects(jarPackage, element);
			if (jarPackage.areGeneratedFilesExported())
				xmlReadManifest(jarPackage, element);
			xmlReadSelectedElements(jarPackage, element);
		}
		return jarPackage;
	}