  public void badRequestDeprecatedHTMLEscaped() {
    HttpQuery query = NettyMocks.getQuery(tsdb, "/");
    query.badRequest(new BadRequestException("<script>alert(document.cookie)</script>"));

    assertEquals(HttpResponseStatus.BAD_REQUEST, query.response().getStatus());
    assertTrue(query.response().getContent().toString(Charset.forName("UTF-8")).contains(
        "The reason provided was:<blockquote>&lt;script&gt;alert(document.cookie)&lt;/script&gt;"
    ));
  }