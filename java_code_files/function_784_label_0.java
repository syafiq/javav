	protected ICStorageElement translateInputStreamToDocument(InputStream input) {
		try {
			Document document = XmlProcessorFactoryCdt.createDocumentBuilderWithErrorOnDOCTYPE().parse(input);
			return XmlStorageUtil.createCStorageTree(document);
		} catch (Exception e) {
			MakeCorePlugin.log(e);
		}
		return null;
	}