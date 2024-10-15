	public static Object bytesToObject(byte[] buf, int offset, int length) {
		if (length <= 0) {
			return null;
		}

		if (Value.DisableDeserializer) {
			throw new AerospikeException.Serialize("Object deserializer has been disabled");
		}

		try (ByteArrayInputStream bastream = new ByteArrayInputStream(buf, offset, length)) {
			try (ObjectInputStream oistream = new ObjectInputStream(bastream)) {
				return oistream.readObject();
			}
		}
		catch (Throwable e) {
			throw new AerospikeException.Serialize(e);
		}
	}