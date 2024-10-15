	private File createDefaultXMLFormat(File xmlFile) {
		Document d;
		try {
			d = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE().newDocument();
		} catch (ParserConfigurationException e) {
			TemplateEngineUtil.log(e);
			return xmlFile;
		}
		Node rootElement = d.appendChild(d.createElement("SharedRoot")); //$NON-NLS-1$
		Element element = (Element) rootElement.appendChild(d.createElement("SharedProperty")); //$NON-NLS-1$
		element.setAttribute(TemplateEngineHelper.ID, ""); //$NON-NLS-1$
		element.setAttribute(TemplateEngineHelper.VALUE, ""); //$NON-NLS-1$

		DOMSource domSource = new DOMSource(d);
		TransformerFactory transFactory = XmlProcessorFactoryCdt.createTransformerFactoryWithErrorOnDOCTYPE();

		try {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(xmlFile);
				Result fileResult = new StreamResult(fos);
				transFactory.newTransformer().transform(domSource, fileResult);
			} finally {
				if (fos != null) {
					fos.close();
				}
			}
		} catch (IOException ioe) {
			TemplateEngineUtil.log(ioe);
		} catch (TransformerConfigurationException tce) {
			TemplateEngineUtil.log(tce);
		} catch (TransformerException te) {
			TemplateEngineUtil.log(te);
		}
		return xmlFile;
	}