	public void setColor(int x, int y, int col) {
		if (x < 0 || x >= width)
			return;

		if (y < 0 || y >= height)
			return;

		gray[y][x] = -1;
		color[y][x] = col;
	}