	public void executeProcedureInternal(TContext context, TMemory memory, List<TValue> args, Map<String, TValue> named)
			throws EaterException, EaterExceptionLocated {
		if (functionType != TFunctionType.PROCEDURE && functionType != TFunctionType.LEGACY_DEFINELONG) {
			throw new IllegalStateException();
		}
		final TMemory copy = getNewMemory(memory, args, named);
		context.executeLines(copy, body, TFunctionType.PROCEDURE, false);
	}