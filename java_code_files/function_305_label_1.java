	public TValue executeReturnFunction(TContext context, TMemory memory, LineLocation location, List<TValue> args,
			Map<String, TValue> named) throws EaterException, EaterExceptionLocated {
		if (functionType == TFunctionType.LEGACY_DEFINE) {
			return executeReturnLegacyDefine(location, context, memory, args);
		}
		if (functionType != TFunctionType.RETURN_FUNCTION) {
			throw EaterException.unlocated("Illegal call here. Is there a return directive in your function?");
		}
		final TMemory copy = getNewMemory(memory, args, named);
		final TValue result = context.executeLines(copy, body, TFunctionType.RETURN_FUNCTION, true);
		if (result == null) {
			throw EaterException.unlocated("No return directive found in your function");
		}
		return result;
	}