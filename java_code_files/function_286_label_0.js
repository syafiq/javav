  void validate(TestContext context, Buffer buffer) {
    context.assertTrue(buffer.toString().contains("CONNECTED"), "Was expected <" + buffer.toString() + "> to contain 'CONNECTED'");
    context.assertTrue(buffer.toString().contains("version:1.2"));

    User user = server.stompHandler().getUserBySession(extractSession(buffer.toString()));
    context.assertNotNull(user);
  }