	private TValue executeReturnLegacyDefine(LineLocation location, TContext context, TMemory memory, List<TValue> args)
			throws EaterException, EaterExceptionLocated {
		if (legacyDefinition == null)
			throw new IllegalStateException();

		final TMemory copy = getNewMemory(memory, args, Collections.<String, TValue>emptyMap());
		final String tmp = context.applyFunctionsAndVariables(copy, location, legacyDefinition);
		if (tmp == null)
			return TValue.fromString("");

		return TValue.fromString(tmp);
		// eaterReturn.execute(context, copy);
		// // System.err.println("s3=" + eaterReturn.getValue2());
		// return eaterReturn.getValue2();
	}