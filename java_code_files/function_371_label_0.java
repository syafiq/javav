    public void isTooLargeShortArrayInputLengthForBitShuffleShuffle() throws Exception {
        BitShuffle.shuffle(new short[Integer.MAX_VALUE / 2 + 1]);

    }