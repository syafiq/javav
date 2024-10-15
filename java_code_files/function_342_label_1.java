	public static boolean isSecurityEnv(String name) {
		return name != null && name.toLowerCase().startsWith("plantuml.security.");
	}