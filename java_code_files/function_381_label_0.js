    public void isTooLargeFloatArrayInputLength() throws Exception {
        Snappy.compress(new float[Integer.MAX_VALUE / 4 + 1]);
    }