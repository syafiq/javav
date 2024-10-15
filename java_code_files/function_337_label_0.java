	public TFunctionImpl(String functionName, List<TFunctionArgument> args, boolean unquoted,
			TFunctionType functionType) {
		final Set<String> names = new HashSet<>();
		for (TFunctionArgument tmp : args)
			names.add(tmp.getName());

		this.signature = new TFunctionSignature(functionName, args.size(), names);
		this.args = args;
		this.unquoted = unquoted;
		this.functionType = functionType;
	}