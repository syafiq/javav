	private Transformer createSerializer() throws CoreException {
		try {
			return TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new CoreException(Util.createStatus(e));
		} catch (TransformerFactoryConfigurationError e) {
			throw new CoreException(Util.createStatus(e));
		}
	}