    public void isTooLargeLongArrayInputLength() throws Exception {
        Snappy.compress(new long[Integer.MAX_VALUE / 8 + 1]);
    }