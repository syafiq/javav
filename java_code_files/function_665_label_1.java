	private void serializePreference(String key, InternalXmlStorageElement element) throws CoreException {
		Document doc = element.fElement.getOwnerDocument();

		// Transform the document to something we can save in a file
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		FileOutputStream fileStream = null;
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(stream);
			transformer.transform(source, result);

			// Save the document
			File file = getPreferenceFile(key);
			String utfString = stream.toString("UTF-8"); //$NON-NLS-1$

			if (file.exists()) {
				//				if (projectFile.isReadOnly()) {
				//
				//					// Inform Eclipse that we are intending to modify this file
				//					// This will provide the user the opportunity, via UI prompts, to fetch the file from source code control
				//					// reset a read-only file protection to write etc.
				//					// If there is no shell, i.e. shell is null, then there will be no user UI interaction
				//
				//					//TODO
				//					//IStatus status = projectFile.getWorkspace().validateEdit(new IFile[]{projectFile}, shell);
				//
				//					// If the file is still read-only, then we should not attempt the write, since it will
				//					// just fail - just throw an exception, to be caught below, and inform the user
				//					// For other non-successful status, we take our chances, attempt the write, and pass
				//					// along any exception thrown
				//
				//					//if (!status.isOK()) {
				//					 //   if (status.getCode() == IResourceStatus.READ_ONLY_LOCAL) {
				//					  //  	stream.close();
				//	    	           //     throw new CoreException(status);
				//					    //}
				//					//}
				//				}
				//				projectFile.setContents(new ByteArrayInputStream(utfString.getBytes("UTF-8")), IResource.FORCE, new NullProgressMonitor());	//$NON-NLS-1$
			} else {
				file.createNewFile();
			}
			fileStream = new FileOutputStream(file);
			byte[] bytes;
			try {
				bytes = utfString.getBytes("UTF-8"); //$NON-NLS-1$
			} catch (UnsupportedEncodingException e) {
				bytes = utfString.getBytes();
			}
			fileStream.write(bytes);
			fileStream.close();
			// Close the streams
			stream.close();
		} catch (TransformerConfigurationException e) {
			throw ExceptionFactory.createCoreException(e);
		} catch (TransformerException e) {
			throw ExceptionFactory.createCoreException(e);
		} catch (IOException e) {
			throw ExceptionFactory.createCoreException(e);
		}
	}