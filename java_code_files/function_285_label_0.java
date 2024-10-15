  public void testWebSocketClientMustBeConnected(TestContext context) {
    Async async = context.async();
    testClientMustBeConnected(context, v -> {
      wsClient.webSocket(8080, "localhost", "/stomp").onComplete(context.asyncAssertSuccess(ws -> {
        Buffer received = Buffer.buffer();
        ws.binaryMessageHandler(received::appendBuffer);
        ws.writeBinaryMessage(
          Buffer.buffer("SEND\n" +
            "destination:/test\n" +
            "\n" +
            "hello" +
            FrameParser.NULL));
        ws.endHandler(v2 -> {
          context.assertTrue(received.toString().startsWith("ERROR\n"));
          async.complete();
        });
      }));
    });
  }