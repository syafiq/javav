	public static BufferedImage retrieveDistributorImage(LicenseInfo licenseInfo) {
		if (licenseInfo.getLicenseType() != LicenseType.DISTRIBUTOR) {
			return null;
		}
		try {
			final byte[] s1 = PLSSignature.retrieveDistributorImageSignature();
			if (SignatureUtils.toHexString(s1).equals(SignatureUtils.toHexString(licenseInfo.sha)) == false) {
				return null;
			}
			final InputStream dis = PSystemVersion.class.getResourceAsStream("/distributor.png");
			if (dis == null) {
				return null;
			}
			try {
				final BufferedImage result = SImageIO.read(dis);
				return result;
			} finally {
				dis.close();
			}
		} catch (Exception e) {
			Logme.error(e);
		}
		return null;
	}