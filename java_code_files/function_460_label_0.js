	private static synchronized SAXParserFactory createSAXFactoryIgnoringDOCTYPE() {
		SAXParserFactory f = SAXParserFactory.newInstance();
		try {
			// ignore DOCTYPE:
			f.setFeature("http://xml.org/sax/features/external-general-entities", false); //$NON-NLS-1$
			f.setFeature("http://xml.org/sax/features/external-parameter-entities", false); //$NON-NLS-1$
			f.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); //$NON-NLS-1$
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return f;
	}