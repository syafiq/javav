    public void isTooLargeIntArrayInputLength() throws Exception {
        Snappy.compress(new int[Integer.MAX_VALUE / 4 + 1]);
    }