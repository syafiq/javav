	private static LicenseInfo retrieve(SFile f) throws IOException {
		final BufferedReader br = f.openBufferedReader();
		if (br == null) {
			return null;
		}
		try {
			final String s = br.readLine();
			final LicenseInfo result = retrieveNamed(s);
			if (result != null) {
				Log.info("Reading license from " + f.getAbsolutePath());
			}
			return result;
		} finally {
			br.close();
		}
	}