	private TMemory getNewMemory(TMemory memory, List<TValue> values, Map<String, TValue> namedArguments) {
		final Map<String, TValue> result = new HashMap<String, TValue>();
		int ivalue = 0;
		for (TFunctionArgument arg : args) {
			final TValue value;
			if (namedArguments.containsKey(arg.getName())) {
				value = namedArguments.get(arg.getName());
			} else if (ivalue < values.size()) {
				value = values.get(ivalue);
				ivalue++;
			} else {
				value = arg.getOptionalDefaultValue();
			}
			if (value == null) {
				throw new IllegalStateException();
			}
			result.put(arg.getName(), value);
		}
		return memory.forkFromGlobal(result);
	}