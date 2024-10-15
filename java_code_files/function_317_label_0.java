	public void finalizeEnddefinelong() {
		if (functionType != TFunctionType.LEGACY_DEFINELONG)
			throw new UnsupportedOperationException();

		if (body.size() == 1) {
			this.functionType = TFunctionType.LEGACY_DEFINE;
			this.legacyDefinition = body.get(0).getString();
		}
	}