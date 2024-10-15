	public static void importIntoTestRunSession(File swapFile, TestRunSession testRunSession) throws CoreException {
		try {
			SAXParserFactory parserFactory= SAXParserFactory.newInstance();
//			parserFactory.setValidating(true); // TODO: add DTD and debug flag
			SAXParser parser= parserFactory.newSAXParser();
			TestRunHandler handler= new TestRunHandler(testRunSession);
			parser.parse(swapFile, handler);
		} catch (ParserConfigurationException | SAXException e) {
			throwImportError(swapFile, e);
		} catch (IOException e) {
			throwImportError(swapFile, e);
		} catch (IllegalArgumentException e) {
			// Bug in parser: can throw IAE even if file is not null
			throwImportError(swapFile, e);
		}
	}