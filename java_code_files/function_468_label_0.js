	public static void exportTestRunSession(TestRunSession testRunSession, OutputStream out)
			throws TransformerFactoryConfigurationError, TransformerException {

		Transformer transformer= XmlProcessorFactoryJdtJunit.createTransformerFactoryWithErrorOnDOCTYPE().newTransformer();
		InputSource inputSource= new InputSource();
		SAXSource source= new SAXSource(new TestRunSessionSerializer(testRunSession), inputSource);
		StreamResult result= new StreamResult(out);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
		transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
		/*
		 * Bug in Xalan: Only indents if proprietary property
		 * org.apache.xalan.templates.OutputProperties.S_KEY_INDENT_AMOUNT is set.
		 *
		 * Bug in Xalan as shipped with J2SE 5.0:
		 * Does not read the indent-amount property at all >:-(.
		 */
		try {
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IllegalArgumentException e) {
			// no indentation today...
		}
		transformer.transform(source, result);
	}