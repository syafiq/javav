    public static byte[] compress(long[] input)
            throws IOException
    {
        int byteSize = input.length * 8;
        if (byteSize < input.length) {
            throw new SnappyError(SnappyErrorCode.TOO_LARGE_INPUT, "input array size is too large: " + input.length);
        }
        return rawCompress(input, byteSize); // long uses 8 bytes
    }