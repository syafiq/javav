	private UserDefinedVariableSupplier() {
		fListeners = Collections.synchronizedSet(new HashSet<ICdtVariableChangeListener>());
	}