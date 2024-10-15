  public void internalError(final Exception cause) {
    logError("Internal Server Error on " + request().getUri(), cause);

    if (this.api_version > 0) {
      // always default to the latest version of the error formatter since we
      // need to return something
      switch (this.api_version) {
        case 1:
        default:
          sendReply(HttpResponseStatus.INTERNAL_SERVER_ERROR,
              serializer.formatErrorV1(cause));
      }
      return;
    }

    ThrowableProxy tp = new ThrowableProxy(cause);
    tp.calculatePackagingData();
    final String pretty_exc = ThrowableProxyUtil.asString(tp);
    tp = null;
    if (hasQueryStringParam("json")) {
      // 32 = 10 + some extra space as exceptions always have \t's to escape.
      final StringBuilder buf = new StringBuilder(32 + pretty_exc.length());
      buf.append("{\"err\":\"");
      HttpQuery.escapeJson(pretty_exc, buf);
      buf.append("\"}");
      sendReply(HttpResponseStatus.INTERNAL_SERVER_ERROR, buf);
    } else {
      sendReply(HttpResponseStatus.INTERNAL_SERVER_ERROR,
                makePage("Internal Server Error", "Houston, we have a problem",
                         "<blockquote>"
                         + "<h1>Internal Server Error</h1>"
                         + "Oops, sorry but your request failed due to a"
                         + " server error.<br/><br/>"
                         + "Please try again in 30 seconds.<pre>"
                         + pretty_exc
                         + "</pre></blockquote>"));
    }
  }