    public static byte[] shuffle(short[] input) throws IOException {
        if (input.length * 2 < input.length) {
            throw new SnappyError(SnappyErrorCode.TOO_LARGE_INPUT, "input array size is too large: " + input.length);
        }
        byte[] output = new byte[input.length * 2];
        int numProcessed = impl.shuffle(input, 0, 2, input.length * 2, output, 0);
        assert(numProcessed == input.length * 2);
        return output;
    }