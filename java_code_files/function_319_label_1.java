	public boolean canCover(int nbArg, Set<String> namedArguments) {
		for (String n : namedArguments) {
			if (signature.getNamedArguments().contains(n) == false) {
				return false;
			}
		}
		if (nbArg > args.size()) {
			return false;
		}
		assert nbArg <= args.size();
		int neededArgument = 0;
		for (TFunctionArgument arg : args) {
			if (namedArguments.contains(arg.getName())) {
				continue;
			}
			if (arg.getOptionalDefaultValue() == null) {
				neededArgument++;
			}
		}
		if (nbArg < neededArgument) {
			return false;
		}
		assert nbArg >= neededArgument;
		return true;
	}