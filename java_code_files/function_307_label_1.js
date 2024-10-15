	public void addBody(StringLocated s) throws EaterExceptionLocated {
		body.add(s);
		if (s.getType() == TLineType.RETURN) {
			this.containsReturn = true;
			if (functionType == TFunctionType.PROCEDURE) {
				throw EaterExceptionLocated
						.located("A procedure cannot have !return directive. Declare it as a function instead ?", s);
				// this.functionType = TFunctionType.RETURN;
			}
		}
	}