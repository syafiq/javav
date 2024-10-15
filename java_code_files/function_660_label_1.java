	private void writeProjectStorageType(IProject project, CProjectDescriptionStorageTypeProxy type)
			throws CoreException {
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			// Set the version
			ProcessingInstruction instruction = doc.createProcessingInstruction(
					ICProjectDescriptionStorageType.STORAGE_VERSION_NAME, type.version.toString());
			doc.appendChild(instruction);
			// Set the type id
			Element el = doc.createElement(ICProjectDescriptionStorageType.STORAGE_ROOT_ELEMENT_NAME);
			el.setAttribute(ICProjectDescriptionStorageType.STORAGE_TYPE_ATTRIBUTE, type.id);
			doc.appendChild(el);
			XmlUtil.prettyFormat(doc);

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
			// Indentation is done with XmlUtil.prettyFormat(doc)
			transformer.setOutputProperty(OutputKeys.INDENT, "no"); //$NON-NLS-1$
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(stream);
			transformer.transform(source, result);

			InputStream input = new ByteArrayInputStream(stream.toByteArray());

			// Set the project description storage type
			IFile f = project.getFile(ICProjectDescriptionStorageType.STORAGE_FILE_NAME);
			if (!f.exists())
				f.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			ensureWritable(f);
			if (!f.exists())
				f.create(input, true, new NullProgressMonitor());
			else
				f.setContents(input, IResource.FORCE, new NullProgressMonitor());
		} catch (ParserConfigurationException e) {
			throw ExceptionFactory.createCoreException(e);
		} catch (TransformerConfigurationException e) {
			throw ExceptionFactory.createCoreException(e);
		} catch (TransformerFactoryConfigurationError e) {
			throw ExceptionFactory.createCoreException(e);
		} catch (TransformerException e) {
			throw ExceptionFactory.createCoreException(e);
		}
	}