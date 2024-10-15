	public void run(ITestModelUpdater modelUpdater, InputStream inputStream) throws TestingException {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			sp.parse(inputStream, new BoostXmlLogHandler(modelUpdater));

		} catch (IOException e) {
			throw new TestingException(
					getErrorText(BoostTestsRunnerMessages.BoostTestsRunner_io_error_prefix, e.getLocalizedMessage()));

		} catch (NumberFormatException e) {
			throw new TestingException(
					getErrorText(BoostTestsRunnerMessages.BoostTestsRunner_xml_error_prefix, e.getLocalizedMessage()));

		} catch (ParserConfigurationException e) {
			throw new TestingException(
					getErrorText(BoostTestsRunnerMessages.BoostTestsRunner_xml_error_prefix, e.getLocalizedMessage()));

		} catch (SAXException e) {
			throw new TestingException(
					getErrorText(BoostTestsRunnerMessages.BoostTestsRunner_xml_error_prefix, e.getLocalizedMessage()));
		}
	}