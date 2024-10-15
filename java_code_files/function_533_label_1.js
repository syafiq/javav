	public void packBlob(Object val) {
		byte[] bytes = BlobValue.serialize(val);
		packByteArrayBegin(bytes.length + 1);
		packByte(ParticleType.JBLOB);
		packByteArray(bytes, 0, bytes.length);
	}