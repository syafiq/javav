	private boolean fileExists(String path) {
		final SFile f = new SFile(path);
		return f.exists();
	}