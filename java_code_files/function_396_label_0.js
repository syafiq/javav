  private static void safeClose(Connection c) {
    try {
      c.close();
    } catch (Exception e) {
      // OK
    }
  }