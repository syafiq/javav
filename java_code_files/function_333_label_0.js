	private boolean forbiddenURL(String full) {
		// Thanks to Agasthya Kasturi
		if (full.contains("@"))
			return true;
		if (full.startsWith("https://") == false && full.startsWith("http://") == false)
			return true;
		if (full.matches("^https?://[-#.0-9:\\[\\]+]+/.*"))
			return true;
		if (full.matches("^https?://[^.]+/.*"))
			return true;
		if (full.matches("^https?://[^.]+$"))
			return true;
		return false;
	}