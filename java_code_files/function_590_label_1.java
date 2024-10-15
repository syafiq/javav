	private void close(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}