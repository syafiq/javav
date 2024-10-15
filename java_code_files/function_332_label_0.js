	public CommandExecutionResult openNetwork(String name) {
		if (initDone == false)
			return errorNoInit();

		if (currentGroup() != null)
			return CommandExecutionResult.error("Cannot open network in a group");

		for (NStackable element : stack)
			if (element instanceof Network)
				return CommandExecutionResult.error("Cannot nest network");

		if (networks.size() == 0 && groups.size() == 0)
			eventuallyConnectAllStandaloneServersToHiddenNetwork();

		final Network network = createNetwork(name);
		stack.add(0, network);
		return CommandExecutionResult.ok();
	}