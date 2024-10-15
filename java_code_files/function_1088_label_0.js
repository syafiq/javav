    public void testAccessWithJavaMethod() {
        PropertyName property =
                CommonFactoryFinder.getFilterFactory().property("java.lang.Thread.sleep(30000)");
        long start = System.currentTimeMillis();
        property.evaluate(Collections.emptyMap());
        long runtime = System.currentTimeMillis() - start;
        assertTrue("java.lang.Thread.sleep(30000) was executed", runtime < 30000);
    }