  public void setSmoothParams() throws Exception {
    assertPlotParam("smooth", "unique");
    assertPlotParam("smooth", "frequency");
    assertPlotParam("smooth", "fnormal");
    assertPlotParam("smooth", "cumulative");
    assertPlotParam("smooth", "cnormal");
    assertPlotParam("smooth", "bins");
    assertPlotParam("smooth", "csplines");
    assertPlotParam("smooth", "acsplines");
    assertPlotParam("smooth", "mcsplines");
    assertPlotParam("smooth", "bezier");
    assertPlotParam("smooth", "sbezier");
    assertPlotParam("smooth", "unwrap");
    assertPlotParam("smooth", "zsort");
    assertInvalidPlotParam("smooth", "[33:system(%20");
  }