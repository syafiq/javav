	public void testParent() throws Exception {
		LambdaSectionParent m = new LambdaSectionParent("ignore");
		String actual = JStachio.render(m);
		String expected = """
				bingo
				LambdaSectionParent[stuff=ignore]
								""";
		assertEquals(expected, actual);
	}