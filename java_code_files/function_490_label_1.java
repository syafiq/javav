	public static void writeProfilesToStream(Collection<Profile> profiles, OutputStream stream, String encoding, IProfileVersioner profileVersioner) throws CoreException {

		try {
			final DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder= factory.newDocumentBuilder();
			final Document document= builder.newDocument();

			final Element rootElement = document.createElement(XML_NODE_ROOT);
			rootElement.setAttribute(XML_ATTRIBUTE_VERSION, Integer.toString(profileVersioner.getCurrentVersion()));

			document.appendChild(rootElement);

			for (Profile profile : profiles) {
				if (profile.isProfileToSave()) {
					final Element profileElement= createProfileElement(profile, document, profileVersioner);
					rootElement.appendChild(profileElement);
				}
			}

			Transformer transformer=TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			transformer.transform(new DOMSource(document), new StreamResult(stream));
		} catch (TransformerException | ParserConfigurationException e) {
			throw createException(e, FormatterMessages.CodingStyleConfigurationBlock_error_serializing_xml_message);
		}
	}