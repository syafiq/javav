  private void testClientMustBeConnected(TestContext context, Handler<Void> cont) {
    client
      .connect(server.actualPort(), "localhost")
      .onComplete(context.asyncAssertSuccess(conn -> {
        Future<String> fut = conn.subscribe("/test", frame -> {
          context.fail("Should not receive a messsage");
        });
        fut.onComplete(context.asyncAssertSuccess(v2 -> {
          cont.handle(null);
        }));
      }));
  }