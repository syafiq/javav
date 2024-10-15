	public void read(InputStream is) throws IOException {
		try {
			parser = new WelcomeParser();
		} catch (ParserConfigurationException | SAXException e) {
			throw (IOException) (new IOException().initCause(e));
		}
		parser.parse(is);
	}