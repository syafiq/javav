	public void dumpDirect(Path output) {
		LOGGER.info("dumping " + this.id + "'s assets and data");
		// data dump time
		try {
			for(Map.Entry<List<String>, Supplier<byte[]>> e : this.root.entrySet()) {
				String pathStr = String.join("/", e.getKey());
				Path path = output.resolve(pathStr);
				if(path.startsWith(output)) {
					Files.createDirectories(path.getParent());
					Files.write(path, e.getValue().get());
				} else {
					LOGGER.error("RRP contains out-of-directory path! \"" + pathStr + "\"");
				}
			}
			
			Path assets = output.resolve("assets");
			Files.createDirectories(assets);
			for(Map.Entry<Identifier, Supplier<byte[]>> entry : this.assets.entrySet()) {
				this.write(assets, entry.getKey(), entry.getValue().get());
			}
			
			Path data = output.resolve("data");
			Files.createDirectories(data);
			for(Map.Entry<Identifier, Supplier<byte[]>> entry : this.data.entrySet()) {
				this.write(data, entry.getKey(), entry.getValue().get());
			}
		} catch(IOException exception) {
			throw new RuntimeException(exception);
		}
	}