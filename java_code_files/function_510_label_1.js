		public static byte[] serialize(Object val) {
			if (DisableSerializer) {
				throw new AerospikeException("Object serializer has been disabled");
			}

			try (ByteArrayOutputStream bstream = new ByteArrayOutputStream()) {
				try (ObjectOutputStream ostream = new ObjectOutputStream(bstream)) {
					ostream.writeObject(val);
				}
				return bstream.toByteArray();
			}
			catch (Throwable e) {
				throw new AerospikeException.Serialize(e);
			}
		}