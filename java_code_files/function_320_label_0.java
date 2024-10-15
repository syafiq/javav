	public UImage toUImage(ColorMapper colorMapper, HColor backcolor, HColor forecolor) {
		final BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		if (backcolor == null)
			backcolor = HColors.WHITE;

		if (forecolor == null)
			forecolor = HColors.BLACK;

		final HColorGradient gradient = HColors.gradient(backcolor, forecolor, '\0');
		for (int col = 0; col < width; col++) {
			for (int line = 0; line < height; line++) {
				final int localColor = color[line][col];
				if (localColor == -1) {
					final double coef = 1.0 * gray[line][col] / (16 - 1);
					final Color c = gradient.getColor(colorMapper, coef, 255);
					im.setRGB(col, line, c.getRGB());
				} else {
					im.setRGB(col, line, localColor);
				}
			}
		}
		return new UImage(new PixelImage(im, AffineTransformType.TYPE_BILINEAR));
	}