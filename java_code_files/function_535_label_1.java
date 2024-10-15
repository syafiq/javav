	public LuaValue getLuaValue(int type, byte[] buf, int offset, int len) throws AerospikeException {
		if (len <= 0) {
			return LuaValue.NIL;
		}

		switch (type) {
		case ParticleType.STRING:
			byte[] copy = new byte[len];
			System.arraycopy(buf, offset, copy, 0, len);
			return LuaString.valueOf(copy, 0, len);

		case ParticleType.INTEGER:
			if (len <= 4) {
				return LuaInteger.valueOf(Buffer.bytesToInt(buf, offset));
			}

			if (len <= 8) {
				return LuaInteger.valueOf(Buffer.bytesToLong(buf, offset));
			}
			throw new AerospikeException("Lua BigInteger not implemented.");

		case ParticleType.BOOL:
			return LuaBoolean.valueOf(Buffer.bytesToBool(buf, offset, len));

		case ParticleType.DOUBLE:
			return LuaDouble.valueOf(Buffer.bytesToDouble(buf, offset));

		case ParticleType.BLOB:
			byte[] blob = new byte[len];
			System.arraycopy(buf, offset, blob, 0, len);
			return new LuaBytes(this, blob);

		case ParticleType.JBLOB:
			Object object = Buffer.bytesToObject(buf, offset, len);
			return new LuaJavaBlob(object);

		case ParticleType.LIST: {
			LuaUnpacker unpacker = new LuaUnpacker(this, buf, offset, len);
			return unpacker.unpackList();
		}

		case ParticleType.MAP: {
			LuaUnpacker unpacker = new LuaUnpacker(this, buf, offset, len);
			return unpacker.unpackMap();
		}

		case ParticleType.GEOJSON:
			// skip the flags
			int ncells = Buffer.bytesToShort(buf, offset + 1);
			int hdrsz = 1 + 2 + (ncells * 8);
			return new LuaGeoJSON(new String(buf, offset + hdrsz, len - hdrsz));

		default:
			return LuaValue.NIL;
		}
	}