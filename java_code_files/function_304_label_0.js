	public FilesEntry addRawEntry(String raw) {
		final int x = raw.indexOf('/');
		if (x == -1) {
			final FilesEntry result = new FilesEntry(raw, FilesType.DATA);
			children.add(result);
			return result;
		}
		final FilesEntry folder = getOrCreateFolder(raw.substring(0, x));
		final String remain = raw.substring(x + 1);
		if (remain.length() == 0)
			return folder;
		return folder.addRawEntry(remain);
	}