	public void add(String line) {
		if (line.startsWith("/"))
			tmp.add(line.substring(1));
	}