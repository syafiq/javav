    public void isTooLargeDoubleArrayInputLengthForBitShuffleShuffle() throws Exception {
        BitShuffle.shuffle(new double[Integer.MAX_VALUE / 8 + 1]);
    }