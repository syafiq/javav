	private static void writeProfilesToStream(Collection<Profile> profiles, OutputStream stream, String encoding,
			IProfileVersioner profileVersioner) throws CoreException {
		try {
			final DocumentBuilder builder = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			final Document document = builder.newDocument();

			final Element rootElement = document.createElement(XML_NODE_ROOT);
			rootElement.setAttribute(XML_ATTRIBUTE_VERSION, Integer.toString(profileVersioner.getCurrentVersion()));

			document.appendChild(rootElement);

			for (Object element : profiles) {
				final Profile profile = (Profile) element;
				if (profile.isProfileToSave()) {
					final Element profileElement = createProfileElement(profile, document, profileVersioner);
					rootElement.appendChild(profileElement);
				}
			}

			Transformer transformer = XmlProcessorFactoryCdt.createTransformerFactoryWithErrorOnDOCTYPE()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			transformer.transform(new DOMSource(document), new StreamResult(stream));
		} catch (TransformerException e) {
			throw createException(e, FormatterMessages.CodingStyleConfigurationBlock_error_serializing_xml_message);
		} catch (ParserConfigurationException e) {
			throw createException(e, FormatterMessages.CodingStyleConfigurationBlock_error_serializing_xml_message);
		}
	}