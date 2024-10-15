	public void drawU(UGraphic ug) {
		for (String s : tmp) {
			final Display display = Display.getWithNewlines("<:1f4c4:>" + s);
			TextBlock result = display.create(fontConfiguration, HorizontalAlignment.LEFT, skinParam);
			result.drawU(ug);
			ug = ug.apply(UTranslate.dy(result.calculateDimension(ug.getStringBounder()).getHeight()));
		}

	}