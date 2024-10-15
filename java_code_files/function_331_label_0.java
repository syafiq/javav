	private void eventuallyConnectAllStandaloneServersToHiddenNetwork() {
		Network first = null;
		for (NServer server : servers.values())
			if (server.isAlone()) {
				if (first == null) {
					first = createNetwork("");
					first.goInvisible();
				}
				server.connectMeIfAlone(first);
			}
	}