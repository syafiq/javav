	public void packObject(Object obj) {
		if (obj == null) {
			packNil();
			return;
		}

		if (obj instanceof Value) {
			Value value = (Value)obj;
			value.pack(this);
			return;
		}

		if (obj instanceof byte[]) {
			packParticleBytes((byte[])obj);
			return;
		}

		if (obj instanceof String) {
			packParticleString((String)obj);
			return;
		}

		if (obj instanceof Integer) {
			packInt((Integer)obj);
			return;
		}

		if (obj instanceof Long) {
			packLong((Long)obj);
			return;
		}

		if (obj instanceof List<?>) {
			packList((List<?>)obj);
			return;
		}

		if (obj instanceof Map<?,?>) {
			packMap((Map<?,?>)obj);
			return;
		}

		if (obj instanceof Double) {
			packDouble((Double)obj);
			return;
		}

		if (obj instanceof Float) {
			packFloat((Float)obj);
			return;
		}

		if (obj instanceof Short) {
			packInt((Short)obj);
			return;
		}

		if (obj instanceof Boolean) {
			packBoolean((Boolean)obj);
			return;
		}

		if (obj instanceof Byte) {
			packInt(((Byte)obj) & 0xff);
			return;
		}

		if (obj instanceof Character) {
			packInt(((Character)obj).charValue());
			return;
		}

		if (obj instanceof Enum) {
			packString(obj.toString());
			return;
		}

		if (obj instanceof UUID) {
			packString(obj.toString());
			return;
		}

		if (obj instanceof ByteBuffer) {
			packByteBuffer((ByteBuffer) obj);
			return;
		}

		throw new AerospikeException("Unsupported type: " + obj.getClass().getName());
	}