	public void addNullValue() {
		Version version = Version.getServerVersion(client, null);

		// Do not run on servers < 3.6.1
		if (version.isLess(3, 6, 1)) {
			return;
		}

		Key key = new Key(args.namespace, args.set, "addkey");
		String binName = "addbin";

		// Delete record if it already exists.
		client.delete(null, key);

		Bin bin = new Bin(binName, (Long)null);

		AerospikeException ae = assertThrows(AerospikeException.class, new ThrowingRunnable() {
			public void run() {
				client.add(null, key, bin);
			}
		});

		assertEquals(ae.getResultCode(), ResultCode.PARAMETER_ERROR);
	}