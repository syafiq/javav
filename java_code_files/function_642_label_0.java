	private static Document loadXml(InputStream xmlStream) throws CoreException {
		try {
			DocumentBuilder builder = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE();
			return builder.parse(xmlStream);
		} catch (Exception e) {
			throw new CoreException(CCorePlugin.createStatus(Messages.XmlUtil_InternalErrorLoading, e));
		}
	}