    public static byte[] compress(long[] input)
            throws IOException
    {
        return rawCompress(input, input.length * 8); // long uses 8 bytes
    }