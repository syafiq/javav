	public Iterator<FilesEntry> iterator() {
		return Collections.unmodifiableCollection(children).iterator();
	}