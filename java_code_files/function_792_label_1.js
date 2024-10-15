	public void saveDiscoveredScannerInfoToState(IProject project, InfoContext context,
			IDiscoveredScannerInfoSerializable serializable) throws CoreException {
		Document document = getDocument(project);
		// Create document
		try {
			saveDiscoveredScannerInfo(context, serializable, document);

			// Transform the document to something we can save in a file
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(stream);
			transformer.transform(source, result);

			// Save the document
			try {
				IPath path = getDiscoveredScannerConfigStore(project);
				FileOutputStream file = new FileOutputStream(path.toFile());
				file.write(stream.toByteArray());
				file.close();
			} catch (IOException e) {
				throw new CoreException(new Status(IStatus.ERROR, MakeCorePlugin.getUniqueIdentifier(), -1,
						MakeMessages.getString("DiscoveredPathManager.File_Error_Message"), e)); //$NON-NLS-1$
			}

			// Close the streams
			stream.close();
		} catch (TransformerException e) {
			MakeCorePlugin.log(e);
			throw new CoreException(new Status(IStatus.ERROR, MakeCorePlugin.getUniqueIdentifier(), -1,
					MakeMessages.getString("DiscoveredPathManager.File_Error_Message"), e)); //$NON-NLS-1$
		} catch (IOException e) {
			MakeCorePlugin.log(e);
			throw new CoreException(new Status(IStatus.ERROR, MakeCorePlugin.getUniqueIdentifier(), -1,
					MakeMessages.getString("DiscoveredPathManager.File_Error_Message"), e)); //$NON-NLS-1$
		}
	}