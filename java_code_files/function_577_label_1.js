	public static Map decodeCodeFormatterOptions(String fileName, String profileName) {
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			final DecodeCodeFormatterPreferences preferences = new DecodeCodeFormatterPreferences(profileName);
			saxParser.parse(new File(fileName), preferences);
			return preferences.getEntries();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}