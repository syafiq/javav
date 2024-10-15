	private Document getDocument(IProject project) throws CoreException {
		// Get the document
		Reference<Document> ref = fDocumentCache.get(project);
		Document document = ref != null ? ref.get() : null;
		if (document == null) {
			try {
				DocumentBuilder builder = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
				IPath path = getDiscoveredScannerConfigStore(project);
				if (path.toFile().exists()) {
					// read form file
					FileInputStream file = new FileInputStream(path.toFile());
					document = builder.parse(file);
					Node rootElem = document.getFirstChild();
					if (rootElem.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE) {
						// no version info; upgrade
						upgradeDocument(document, project);
					}
				} else {
					// create new document
					document = builder.newDocument();
					ProcessingInstruction pi = document.createProcessingInstruction(SCD_STORE_VERSION, "version=\"2\""); //$NON-NLS-1$
					document.appendChild(pi);
					Element rootElement = document.createElement(SI_ELEM);
					rootElement.setAttribute(ID_ATTR, CDESCRIPTOR_ID);
					document.appendChild(rootElement);
				}
				fDocumentCache.put(project, new SoftReference<>(document));
			} catch (IOException e) {
				MakeCorePlugin.log(e);
				throw new CoreException(new Status(IStatus.ERROR, MakeCorePlugin.getUniqueIdentifier(), -1,
						MakeMessages.getString("DiscoveredPathManager.File_Error_Message"), e)); //$NON-NLS-1$
			} catch (ParserConfigurationException e) {
				MakeCorePlugin.log(e);
				throw new CoreException(new Status(IStatus.ERROR, MakeCorePlugin.getUniqueIdentifier(), -1,
						MakeMessages.getString("DiscoveredPathManager.File_Error_Message"), e)); //$NON-NLS-1$
			} catch (SAXException e) {
				MakeCorePlugin.log(e);
				throw new CoreException(new Status(IStatus.ERROR, MakeCorePlugin.getUniqueIdentifier(), -1,
						MakeMessages.getString("DiscoveredPathManager.File_Error_Message"), e)); //$NON-NLS-1$
			}
		}
		return document;
	}