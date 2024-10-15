	public static LicenseInfo retrieveDistributor() {
		final InputStream dis = PSystemVersion.class.getResourceAsStream("/distributor.txt");
		if (dis == null) {
			return null;
		}
		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			final String licenseString = br.readLine();
			br.close();
			final LicenseInfo result = PLSSignature.retrieveDistributor(licenseString);
			final Throwable creationPoint = new Throwable();
			creationPoint.fillInStackTrace();
			for (StackTraceElement ste : creationPoint.getStackTrace()) {
				if (ste.toString().contains(result.context)) {
					return result;
				}
			}
			return null;
		} catch (Exception e) {
			Logme.error(e);
			return null;
		}
	}