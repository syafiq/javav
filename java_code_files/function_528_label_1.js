	private T unpackBlob(int count) throws IOException, ClassNotFoundException {
		int type = buffer[offset++] & 0xff;
		count--;
		T val;

		switch (type) {
		case ParticleType.STRING:
			val = getString(Buffer.utf8ToString(buffer, offset, count));
			break;

		case ParticleType.JBLOB:
			if (Value.DisableDeserializer) {
				throw new AerospikeException.Serialize("Object deserializer has been disabled");
			}

			try (ByteArrayInputStream bastream = new ByteArrayInputStream(buffer, offset, count)) {
				try (ObjectInputStream oistream = new ObjectInputStream(bastream)) {
					val = getJavaBlob(oistream.readObject());
				}
			}
			catch (Throwable e) {
				throw new AerospikeException.Serialize(e);
			}
			break;

		case ParticleType.GEOJSON:
			val = getGeoJSON(Buffer.utf8ToString(buffer, offset, count));
			break;

		default:
			val = getBlob(Arrays.copyOfRange(buffer, offset, offset + count));
			break;
		}
		offset += count;
		return val;
	}