	public void saveActionData() {
		String actionData = ""; //$NON-NLS-1$

		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = dfactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("tracepointActionData"); //$NON-NLS-1$
			doc.appendChild(rootElement);

			for (Iterator<ITracepointAction> iter = getActions().iterator(); iter.hasNext();) {
				ITracepointAction action = iter.next();

				Element element = doc.createElement("actionEntry"); //$NON-NLS-1$
				element.setAttribute("name", action.getName()); //$NON-NLS-1$
				element.setAttribute("class", action.getClass().getName()); //$NON-NLS-1$
				element.setAttribute("value", action.getMemento()); //$NON-NLS-1$
				rootElement.appendChild(element);

			}

			ByteArrayOutputStream s = new ByteArrayOutputStream();

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

			DOMSource source = new DOMSource(doc);
			StreamResult outputTarget = new StreamResult(s);
			transformer.transform(source, outputTarget);

			actionData = s.toString("UTF8"); //$NON-NLS-1$

		} catch (Exception e) {
			e.printStackTrace();
		}
		GdbPlugin.getDefault().getPluginPreferences().setValue(TRACEPOINT_ACTION_DATA, actionData);
		GdbPlugin.getDefault().savePluginPreferences();
	}