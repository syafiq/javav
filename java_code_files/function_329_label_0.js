	public void setGray(int x, int y, int level) {
		if (x < 0 || x >= width)
			return;

		if (y < 0 || y >= height)
			return;

		if (level < 0 || level >= 16)
			throw new IllegalArgumentException();

		gray[y][x] = level;
		color[y][x] = -1;
	}