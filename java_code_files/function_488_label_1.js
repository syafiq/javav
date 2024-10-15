	public static List<Profile> readProfilesFromStream(InputSource inputSource) throws CoreException {

		final ProfileDefaultHandler handler= new ProfileDefaultHandler();
		try {
		    final SAXParserFactory factory= SAXParserFactory.newInstance();
			final SAXParser parser= factory.newSAXParser();
			parser.parse(inputSource, handler);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw createException(e, FormatterMessages.CodingStyleConfigurationBlock_error_reading_xml_message);
		}
		return handler.getProfiles();
	}