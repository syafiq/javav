    public void isTooLargeShortArrayInputLength() throws Exception {
        Snappy.compress(new short[Integer.MAX_VALUE / 2 + 1]);
    }