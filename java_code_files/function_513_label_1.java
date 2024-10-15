		public int estimateSize() throws AerospikeException.Serialize {
			bytes = serialize(object);
			return bytes.length;
		}