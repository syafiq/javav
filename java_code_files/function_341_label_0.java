	public boolean canWeReadThisEnvironmentVariable(String name) {
		if (name == null)
			return false;

		if (this == UNSECURE)
			return true;
		
		if (name.toLowerCase().startsWith("plantuml"))
			return true;
		
		return true;
	}