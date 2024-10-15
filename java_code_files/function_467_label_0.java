	public static SAXParserFactory createSAXFactoryWithErrorOnDOCTYPE() {
		SAXParserFactory f = SAXParserFactory.newInstance();
		try {
			// force org.xml.sax.SAXParseException for any DOCTYPE:
			f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return f;
	}