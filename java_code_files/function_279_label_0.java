  public void testTCPClientMustBeConnected(TestContext context) {
    Async async = context.async();
    NetClient client = vertx.createNetClient();
    testClientMustBeConnected(context, v -> {
      client.connect(server.actualPort(), "0.0.0.0").onComplete(context.asyncAssertSuccess(so -> {
        Buffer received = Buffer.buffer();
        so.handler(received::appendBuffer);
        so.write(
          "SEND\n" +
            "destination:/test\n" +
            "\n" +
            "hello" +
            FrameParser.NULL);
        so.endHandler(v2 -> {
          context.assertTrue(received.toString().startsWith("ERROR\n"));
          async.complete();
        });
      }));
    });
  }