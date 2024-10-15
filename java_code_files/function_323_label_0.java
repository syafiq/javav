	public UGraphic drawAndMove(UGraphic ug, FontConfiguration fontConfiguration, ISkinParam skinParam, double deltax) {
		final Display display = Display.getWithNewlines(getEmoticon() + getName());
		TextBlock result = display.create(fontConfiguration, HorizontalAlignment.LEFT, skinParam);
		result.drawU(ug.apply(UTranslate.dx(deltax)));
		ug = ug.apply(UTranslate.dy(result.calculateDimension(ug.getStringBounder()).getHeight() + 2));
		for (FilesEntry child : children)
			ug = child.drawAndMove(ug, fontConfiguration, skinParam, deltax + 21);
		return ug;
	}