	public Element createXML(JavadocOptionsManager store) throws ParserConfigurationException {
		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		DocumentBuilder docBuilder= factory.newDocumentBuilder();
		Document document= docBuilder.newDocument();

		// Create the document
		Element project= document.createElement("project"); //$NON-NLS-1$
		document.appendChild(project);

		project.setAttribute("default", "javadoc"); //$NON-NLS-1$ //$NON-NLS-2$

		Element javadocTarget= document.createElement("target"); //$NON-NLS-1$
		project.appendChild(javadocTarget);
		javadocTarget.setAttribute("name", "javadoc"); //$NON-NLS-1$ //$NON-NLS-2$

		Element xmlJavadocDesc= document.createElement("javadoc"); //$NON-NLS-1$
		javadocTarget.appendChild(xmlJavadocDesc);

		if (!store.isFromStandard())
			xmlWriteDoclet(store, document, xmlJavadocDesc);
		else
			xmlWriteJavadocStandardParams(store, document, xmlJavadocDesc);

		return xmlJavadocDesc;
	}