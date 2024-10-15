	public static TestRunSession importTestRunSession(File file) throws CoreException {
		try {
			SAXParserFactory parserFactory= SAXParserFactory.newInstance();
//			parserFactory.setValidating(true); // TODO: add DTD and debug flag
			SAXParser parser= parserFactory.newSAXParser();
			TestRunHandler handler= new TestRunHandler();
			parser.parse(file, handler);
			TestRunSession session= handler.getTestRunSession();
			JUnitCorePlugin.getModel().addTestRunSession(session);
			return session;
		} catch (ParserConfigurationException | SAXException e) {
			throwImportError(file, e);
		} catch (IOException e) {
			throwImportError(file, e);
		} catch (IllegalArgumentException e) {
			// Bug in parser: can throw IAE even if file is not null
			throwImportError(file, e);
		}
		return null; // does not happen
	}