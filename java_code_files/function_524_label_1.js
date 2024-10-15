	public static Object bytesToParticle(int type, byte[] buf, int offset, int len)
		throws AerospikeException {

		switch (type) {
		case ParticleType.STRING:
			return Buffer.utf8ToString(buf, offset, len);

		case ParticleType.INTEGER:
			return Buffer.bytesToNumber(buf, offset, len);

		case ParticleType.BOOL:
			return Buffer.bytesToBool(buf, offset, len);

		case ParticleType.DOUBLE:
			return Buffer.bytesToDouble(buf, offset);

		case ParticleType.BLOB:
			return Arrays.copyOfRange(buf, offset, offset+len);

		case ParticleType.JBLOB:
			return Buffer.bytesToObject(buf, offset, len);

		case ParticleType.GEOJSON:
			return Buffer.bytesToGeoJSON(buf, offset, len);

		case ParticleType.HLL:
			return Buffer.bytesToHLL(buf, offset, len);

		case ParticleType.LIST:
			return Unpacker.unpackObjectList(buf, offset, len);

		case ParticleType.MAP:
			return Unpacker.unpackObjectMap(buf, offset, len);

		default:
			return null;
		}
	}