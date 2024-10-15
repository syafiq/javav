  public void setUp(TestContext context) {
    vertx = rule.vertx();
    AuthenticationProvider provider = PropertyFileAuthentication.create(vertx, "test-auth.properties");
    server = StompServer.create(vertx, new StompServerOptions().setSecured(true))
        .handler(StompServerHandler.create(vertx).authProvider(provider));
    server.listen().onComplete(context.asyncAssertSuccess());
  }