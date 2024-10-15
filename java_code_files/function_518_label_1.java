	public static Bin asBlob(String name, Object value) {
		return new Bin(name, Value.getAsBlob(value));
	}