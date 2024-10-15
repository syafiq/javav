	private boolean isAllowed(AFile file) throws IOException {
		// ::comment when __CORE__
		if (OptionFlags.ALLOW_INCLUDE)
			return true;

		if (file != null) {
			final SFile folder = file.getSystemFolder();
			// System.err.println("canonicalPath=" + path + " " + folder + " " +
			// INCLUDE_PATH);
			if (includePath().contains(folder))
				return true;

		}
		// ::done
		return false;
	}