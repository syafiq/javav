    public void isTooLargeFloatArrayInputLengthForBitShuffleShuffle() throws Exception {
        BitShuffle.shuffle(new float[Integer.MAX_VALUE / 4 + 1]);
    }