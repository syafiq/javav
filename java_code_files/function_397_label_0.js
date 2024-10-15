  protected void releaseResources() throws IOException {
    deleteQueue(q);
    super.releaseResources();
  }