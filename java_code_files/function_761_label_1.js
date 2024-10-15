	public String getMemento() {
		String executeData = ""; //$NON-NLS-1$
		if (externalToolName != null) {
			DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = null;
			try {
				docBuilder = dfactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();

				Element rootElement = doc.createElement("launchConfigName"); //$NON-NLS-1$
				rootElement.setAttribute("configName", externalToolName); //$NON-NLS-1$

				doc.appendChild(rootElement);

				ByteArrayOutputStream s = new ByteArrayOutputStream();

				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer();
				transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
				transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

				DOMSource source = new DOMSource(doc);
				StreamResult outputTarget = new StreamResult(s);
				transformer.transform(source, outputTarget);

				executeData = s.toString("UTF8"); //$NON-NLS-1$

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return executeData;
	}