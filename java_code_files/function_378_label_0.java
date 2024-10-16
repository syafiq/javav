    public static byte[] compress(short[] input)
            throws IOException
    {
        int byteSize = input.length * 2;
        if (byteSize < input.length) {
            throw new SnappyError(SnappyErrorCode.TOO_LARGE_INPUT, "input array size is too large: " + input.length);
        }
        return rawCompress(input, byteSize); // short uses 2 bytes
    }