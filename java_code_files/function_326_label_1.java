	public AFile getAFile(String nameOrPath) throws IOException {
		final SFile filecurrent;
		// Log.info("AParentFolderRegular::looking for " + nameOrPath);
		// Log.info("AParentFolderRegular::dir = " + dir);
		if (dir == null) {
			filecurrent = new SFile(nameOrPath);
		} else {
			filecurrent = dir.getAbsoluteFile().file(nameOrPath);
		}
		// Log.info("AParentFolderRegular::Filecurrent " + filecurrent);
		if (filecurrent.exists()) {
			return new AFileRegular(filecurrent.getCanonicalFile());
		}
		return null;
	}