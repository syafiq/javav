    public void testCountXPathNodesWithJavaMethod() {
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                XmlXpathUtilites.countXPathNodes(
                                        null, "java.lang.Thread.sleep(30000)", null));
        assertEquals("Error reading xpath java.lang.Thread.sleep(30000)", exception.getMessage());
    }