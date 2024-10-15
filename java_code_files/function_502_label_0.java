	public Element readXML() throws IOException, SAXException {

		DocumentBuilderFactory factory= XmlProcessorFactoryJdtUi.createDocumentBuilderFactoryWithErrorOnDOCTYPE();
		factory.setValidating(false);
		DocumentBuilder parser= null;
		try {
			parser= factory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			throw new IOException(ex.getMessage());
		} finally {
			// Note: Above code is OK since clients are responsible to close the stream
		}

		//find the project associated with the ant script
		parser.setErrorHandler(new DefaultHandler());
		Element xmlJavadocDesc= parser.parse(new InputSource(fInputStream)).getDocumentElement();

		NodeList targets= xmlJavadocDesc.getChildNodes();

		for (int i= 0; i < targets.getLength(); i++) {
			Node target= targets.item(i);

			//look through the XML file for the javadoc task
			if ("target".equals(target.getNodeName())) { //$NON-NLS-1$
				NodeList children= target.getChildNodes();
				for (int j= 0; j < children.getLength(); j++) {
					Node child= children.item(j);
					if ("javadoc".equals(child.getNodeName()) && (child.getNodeType() == Node.ELEMENT_NODE)) { //$NON-NLS-1$
						return (Element) child;
					}
				}
			}

		}
		return null;
	}