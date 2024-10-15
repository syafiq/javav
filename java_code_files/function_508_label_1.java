		public void validateKeyType() {
			throw new AerospikeException(ResultCode.PARAMETER_ERROR, "Invalid key type: jblob");
		}