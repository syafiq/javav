  public void setWXH() throws Exception {
    assertPlotDimension("wxh",  "720x640");
    assertInvalidPlotDimension("wxh", "720%0ax640");
  }