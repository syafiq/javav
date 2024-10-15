    public void invalidBlockSize()
            throws Exception
    {
        // We rely on catch below, if there is no error this test will pass
        // This can be done better with Assertions.assertThrows
        Boolean exceptionThrown = false;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        SnappyOutputStream os = new SnappyOutputStream(b, 1024 * 1024 * 1024);
    }