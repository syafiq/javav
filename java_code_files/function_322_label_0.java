	private FilesEntry getOrCreateFolder(String folderName) {
		for (FilesEntry child : children)
			if (child.type == FilesType.FOLDER && child.getName().equals(folderName))
				return child;

		final FilesEntry result = new FilesEntry(folderName, FilesType.FOLDER);
		children.add(result);
		return result;
	}