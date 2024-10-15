	private void write(Path dir, Identifier identifier, byte[] data) {
		try {
			String namespace = identifier.getNamespace();
			String path = identifier.getPath();
			Path file = dir.resolve(namespace).resolve(path);
			if(file.startsWith(dir)) {
				Files.createDirectories(file.getParent());
				try(OutputStream output = Files.newOutputStream(file)) {
					output.write(data);
				}
			} else {
				LOGGER.error("RRP contains out-of-directory location! \"" + namespace + "/" + path + "\"");
			}

		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}