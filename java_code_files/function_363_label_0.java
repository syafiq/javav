    public static byte[] shuffle(int[] input) throws IOException {
        if (input.length * 4 < input.length) {
            throw new SnappyError(SnappyErrorCode.TOO_LARGE_INPUT, "input array size is too large: " + input.length);
        }
        byte[] output = new byte[input.length * 4];
        int numProcessed = impl.shuffle(input, 0, 4, input.length * 4, output, 0);
        assert(numProcessed == input.length * 4);
        return output;
    }