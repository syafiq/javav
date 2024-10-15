	public static Collection<SFile> fileCandidates() {
		final Set<SFile> result = new TreeSet<>();
		final String classpath = System.getProperty("java.class.path");
		String[] classpathEntries = classpath.split(SFile.pathSeparator);
		for (String s : classpathEntries) {
			if (s == null)
				continue;
			SFile dir = new SFile(s);
			if (dir.isFile())
				dir = dir.getParentFile();

			if (dir != null && dir.isDirectory())
				result.add(dir.file("license.txt"));

		}
		return result;
	}