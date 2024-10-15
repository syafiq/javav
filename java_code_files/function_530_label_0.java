	public void runListRangeExample(AerospikeClient client, Parameters params) {
		Key key = new Key(params.namespace, params.set, "mapkey");
		String binName = "mapbin";

		// Delete record if it already exists.
		client.delete(params.writePolicy, key);

		List<Value> l1 = new ArrayList<Value>();
		l1.add(Value.get(new GregorianCalendar(2018, 1, 1).getTimeInMillis()));
		l1.add(Value.get(1));

		List<Value> l2 = new ArrayList<Value>();
		l2.add(Value.get(new GregorianCalendar(2018, 1, 2).getTimeInMillis()));
		l2.add(Value.get(2));

		List<Value> l3 = new ArrayList<Value>();
		l3.add(Value.get(new GregorianCalendar(2018, 2, 1).getTimeInMillis()));
		l3.add(Value.get(3));

		List<Value> l4 = new ArrayList<Value>();
		l4.add(Value.get(new GregorianCalendar(2018, 2, 2).getTimeInMillis()));
		l4.add(Value.get(4));

		List<Value> l5 = new ArrayList<Value>();
		l5.add(Value.get(new GregorianCalendar(2018, 2, 5).getTimeInMillis()));
		l5.add(Value.get(5));

		Map<Value,Value> inputMap = new HashMap<Value,Value>();
		inputMap.put(Value.get("Charlie"), Value.get(l1));
		inputMap.put(Value.get("Jim"), Value.get(l2));
		inputMap.put(Value.get("John"), Value.get(l3));
		inputMap.put(Value.get("Harry"), Value.get(l4));
		inputMap.put(Value.get("Bill"), Value.get(l5));

		// Write values to empty map.
		Record record = client.operate(params.writePolicy, key,
				MapOperation.putItems(MapPolicy.Default, binName, inputMap)
				);

		console.info("Record: " + record);

		List<Value> end = new ArrayList<Value>();
		end.add(Value.get(new GregorianCalendar(2018, 2, 2).getTimeInMillis()));
		end.add(Value.getAsNull());

		// Delete values < end.
		record = client.operate(params.writePolicy, key,
				MapOperation.removeByValueRange(binName, null, Value.get(end), MapReturnType.COUNT)
				);

		console.info("Record: " + record);
	}