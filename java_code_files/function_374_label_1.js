    public static byte[] compress(float[] input)
            throws IOException
    {
        return rawCompress(input, input.length * 4); // float uses 4 bytes
    }