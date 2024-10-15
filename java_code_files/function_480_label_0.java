	public void writeXML(JarPackageData jarPackage) throws IOException {
		Assert.isNotNull(jarPackage);
		DocumentBuilder docBuilder= null;
		DocumentBuilderFactory factory= XmlProcessorFactoryJdtUi.createDocumentBuilderFactoryWithErrorOnDOCTYPE();
		factory.setValidating(false);
		try {
	    	docBuilder= factory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			throw new IOException(JarPackagerMessages.JarWriter_error_couldNotGetXmlBuilder);
		}
		Document document= docBuilder.newDocument();

		// Create the document
		Element xmlJarDesc= document.createElement(JarPackagerUtil.DESCRIPTION_EXTENSION);
		document.appendChild(xmlJarDesc);
		xmlWriteJarLocation(jarPackage, document, xmlJarDesc);
		xmlWriteOptions(jarPackage, document, xmlJarDesc);
		xmlWriteRefactoring(jarPackage, document, xmlJarDesc);
		xmlWriteSelectedProjects(jarPackage, document, xmlJarDesc);
		if (jarPackage.areGeneratedFilesExported())
			xmlWriteManifest(jarPackage, document, xmlJarDesc);
		xmlWriteSelectedElements(jarPackage, document, xmlJarDesc);

		try {
			// Write the document to the stream
			Transformer transformer= XmlProcessorFactoryJdtUi.createTransformerFactoryWithErrorOnDOCTYPE().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, fEncoding);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4"); //$NON-NLS-1$ //$NON-NLS-2$
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(fOutputStream);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new IOException(JarPackagerMessages.JarWriter_error_couldNotTransformToXML);
		}
	}