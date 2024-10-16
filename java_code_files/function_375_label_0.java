    public static byte[] compress(int[] input)
            throws IOException
    {
        int byteSize = input.length * 4;
        if (byteSize < input.length) {
            throw new SnappyError(SnappyErrorCode.TOO_LARGE_INPUT, "input array size is too large: " + input.length);
        }
        return rawCompress(input, byteSize); // int uses 4 bytes
    }