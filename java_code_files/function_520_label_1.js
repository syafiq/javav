	public static Value get(Object value) {
		if (value == null) {
			return NullValue.INSTANCE;
		}

		if (value instanceof Value) {
			return (Value)value;
		}

		if (value instanceof byte[]) {
			return new BytesValue((byte[])value);
		}

		if (value instanceof String) {
			return new StringValue((String)value);
		}

		if (value instanceof Integer) {
			return new IntegerValue((Integer)value);
		}

		if (value instanceof Long) {
			return new LongValue((Long)value);
		}

		if (value instanceof List<?>) {
			return new ListValue((List<?>)value);
		}

		if (value instanceof Map<?,?>) {
			return new MapValue((Map<?,?>)value);
		}

		if (value instanceof Double) {
			return new DoubleValue((Double)value);
		}

		if (value instanceof Float) {
			return new FloatValue((Float)value);
		}

		if (value instanceof Short) {
			return new ShortValue((Short)value);
		}

		if (value instanceof Boolean) {
			if (UseBoolBin) {
				return new BooleanValue((Boolean)value);
			}
			else {
				return new BoolIntValue((Boolean)value);
			}
		}

		if (value instanceof Byte) {
			return new ByteValue((byte)value);
		}

		if (value instanceof Character) {
			return Value.get(((Character)value).charValue());
		}

		if (value instanceof Enum) {
        	return new StringValue(value.toString());
		}

		if (value instanceof UUID) {
			return new StringValue(value.toString());
		}

		if (value instanceof ByteBuffer) {
			ByteBuffer bb = (ByteBuffer)value;
			return new BytesValue(bb.array());
		}

		return new BlobValue(value);
	}