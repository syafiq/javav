		protected static void saveLibraries(List<CPUserLibraryElement> libraries, File file, String encoding, IProgressMonitor monitor) throws IOException {
			OutputStream stream= new FileOutputStream(file);
			try {
				DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
				factory.setValidating(false);
				DocumentBuilder docBuilder= factory.newDocumentBuilder();
				Document document= docBuilder.newDocument();

				// Create the document
				Element rootElement= document.createElement(TAG_ROOT);
				document.appendChild(rootElement);

				rootElement.setAttribute(TAG_VERSION, CURRENT_VERSION);

				for (CPUserLibraryElement curr : libraries) {
					Element libraryElement= document.createElement(TAG_LIBRARY);
					rootElement.appendChild(libraryElement);

					libraryElement.setAttribute(TAG_NAME, curr.getName());
					libraryElement.setAttribute(TAG_SYSTEMLIBRARY, String.valueOf(curr.isSystemLibrary()));

					for (CPListElement child : curr.getChildren()) {
						Element childElement= document.createElement(TAG_ARCHIVE);
						libraryElement.appendChild(childElement);

						childElement.setAttribute(TAG_ARCHIVE_PATH, child.getPath().toPortableString());
						IPath sourceAttachment= (IPath) child.getAttribute(CPListElement.SOURCEATTACHMENT);
						if (sourceAttachment != null) {
							childElement.setAttribute(TAG_SOURCEATTACHMENT, sourceAttachment.toPortableString());
						}
						String sourceEncoding= (String) child.getAttribute(CPListElement.SOURCE_ATTACHMENT_ENCODING);
						if (sourceEncoding != null) {
							childElement.setAttribute(TAG_SOURCE_ATTACHMENT_ENCODING, sourceEncoding);
						}
						String javadocLocation= (String) child.getAttribute(CPListElement.JAVADOC);
						if (javadocLocation != null) {
							childElement.setAttribute(TAG_JAVADOC, javadocLocation);
						}
						String nativeLibPath= (String) child.getAttribute(CPListElement.NATIVE_LIB_PATH);
						if (nativeLibPath != null) {
							childElement.setAttribute(TAG_NATIVELIB_PATHS, nativeLibPath);
						}
						IAccessRule[] accessRules= (IAccessRule[]) child.getAttribute(CPListElement.ACCESSRULES);
						if (accessRules != null && accessRules.length > 0) {
							Element rulesElement= document.createElement(TAG_ACCESSRULES);
							childElement.appendChild(rulesElement);
							for (IAccessRule rule : accessRules) {
								Element ruleElement= document.createElement(TAG_ACCESSRULE);
								rulesElement.appendChild(ruleElement);
								ruleElement.setAttribute(TAG_RULE_KIND, String.valueOf(rule.getKind()));
								ruleElement.setAttribute(TAG_RULE_PATTERN, rule.getPattern().toPortableString());
							}
						}
					}
				}

				// Write the document to the stream
				Transformer transformer=TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
				transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
				transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4"); //$NON-NLS-1$ //$NON-NLS-2$

				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(stream);
				transformer.transform(source, result);
			} catch (ParserConfigurationException | TransformerException e) {
				throw new IOException(e.getMessage());
			} finally {
				try {
					stream.close();
				} catch (IOException e) {
					// ignore
				}
				if (monitor != null) {
					monitor.done();
				}
			}
		}