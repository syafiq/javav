	public boolean finish(IProjectSettingsWizardPage page) {
		SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();
		TransformerHandler handler = null;
		try {
			handler = factory.newTransformerHandler();
		} catch (TransformerConfigurationException e) {
			CUIPlugin.log(e);
			page.showErrorDialog(Messages.ProjectSettingsExportStrategy_exportError,
					Messages.ProjectSettingsExportStrategy_exportFailed);
			return false;
		}

		// gets a writer for the file that was selected by the user
		FileOutputStream outputStream;
		try {
			outputStream = getFileOutputStream(page);
		} catch (IOException e) {
			page.showErrorDialog(Messages.ProjectSettingsExportStrategy_fileOpenError,
					Messages.ProjectSettingsExportStrategy_couldNotOpen);
			return false;
		}

		// write out the XML header
		Transformer transformer = handler.getTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
		transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); //$NON-NLS-1$ //$NON-NLS-2$
		transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //$NON-NLS-1$

		// stream the results to the writer as text
		handler.setResult(new StreamResult(outputStream));

		List<ISettingsProcessor> exporters = page.getSelectedSettingsProcessors();

		ICConfigurationDescription config = page.getSelectedConfiguration();
		ICFolderDescription projectRoot = config.getRootFolderDescription();

		boolean result = false;
		try {
			AttributesImpl attributes = new AttributesImpl();

			handler.startDocument();
			handler.startElement(NONE, NONE, ROOT_ELEMENT, null);

			for (ISettingsProcessor exporter : exporters) {
				attributes.clear();
				attributes.addAttribute(NONE, NONE, SECTION_NAME_ATTRIBUTE, CDATA, exporter.getSectionName());
				handler.startElement(NONE, NONE, SECTION_ELEMENT, attributes);

				// each exporter is responsible for writing out its own section of the file
				exporter.writeSectionXML(projectRoot, handler);

				handler.endElement(NONE, NONE, SECTION_ELEMENT);
			}

			handler.endElement(NONE, NONE, ROOT_ELEMENT);
			handler.endDocument();

			result = true;
		} catch (SAXException e) {
			CUIPlugin.log(e);
			page.showErrorDialog(Messages.ProjectSettingsExportStrategy_exportError,
					Messages.ProjectSettingsExportStrategy_xmlError);
			result = false;
		} catch (SettingsImportExportException e) {
			CUIPlugin.log(e);
			page.showErrorDialog(Messages.ProjectSettingsExportStrategy_fileOpenError,
					Messages.ProjectSettingsExportStrategy_couldNotOpen);
			result = false;
		}

		URI uri = URIUtil.toURI(page.getDestinationFilePath());
		ResourcesUtil.refreshWorkspaceFiles(uri);

		return result;
	}