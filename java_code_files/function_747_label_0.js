	protected static List<Profile> readProfilesFromStream(InputSource inputSource) throws CoreException {
		final ProfileDefaultHandler handler = new ProfileDefaultHandler();
		try {
			final SAXParser parser = XmlProcessorFactoryCdt.createSAXParserWithErrorOnDOCTYPE();
			parser.parse(inputSource, handler);
		} catch (SAXException e) {
			throw createException(e, FormatterMessages.CodingStyleConfigurationBlock_error_reading_xml_message);
		} catch (IOException e) {
			throw createException(e, FormatterMessages.CodingStyleConfigurationBlock_error_reading_xml_message);
		} catch (ParserConfigurationException e) {
			throw createException(e, FormatterMessages.CodingStyleConfigurationBlock_error_reading_xml_message);
		}
		return handler.getProfiles();
	}