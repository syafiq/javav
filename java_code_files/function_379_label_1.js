    public static byte[] compress(short[] input)
            throws IOException
    {
        return rawCompress(input, input.length * 2); // short uses 2 bytes
    }