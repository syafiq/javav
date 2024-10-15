	public <A extends Output<E>, E extends Exception> void append(A a, char c) throws E {
		switch (c) {
			case '"' -> {
				a.append(QUOT);
			}
			case '&' -> {
				a.append(AMP);
			}
			case '\'' -> {
				a.append(APOS);
			}
			case '<' -> {
				a.append(LT);
			}
			case '=' -> {
				a.append(EQUAL);
			}
			case '>' -> {
				a.append(GT);
			}
			case '`' -> {
				a.append(BACK_TICK);
			}
			default -> {
				a.append(c);
			}
		}
	}