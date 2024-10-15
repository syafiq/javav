	public static Value getAsBlob(Object value) {
		return (value == null)? NullValue.INSTANCE : new BlobValue(value);
	}