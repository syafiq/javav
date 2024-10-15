	public static Map decodeCodeFormatterOptions(String zipFileName, String zipEntryName, String profileName) {
		ZipFile zipFile = null;
		BufferedInputStream inputStream = null;
		try {
			zipFile = new ZipFile(zipFileName);
			ZipEntry zipEntry = zipFile.getEntry(zipEntryName);
			if (zipEntry == null) {
				return null;
			}
			inputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			final DecodeCodeFormatterPreferences preferences = new DecodeCodeFormatterPreferences(profileName);
			saxParser.parse(inputStream, preferences);
			return preferences.getEntries();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (IOException e1) {
				// Do nothing
			}
		}
		return null;
	}