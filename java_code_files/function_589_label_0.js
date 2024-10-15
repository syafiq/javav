	private Document getDom(Reader reader) {
		try (reader) {
			return org.eclipse.core.internal.runtime.XmlProcessorFactory
					.parseWithErrorOnDOCTYPE(new InputSource(reader));
		} catch (ParserConfigurationException | IOException | SAXException e) {
			throw new IllegalArgumentException(e);
		}
	}