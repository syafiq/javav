    public static byte[] compress(double[] input)
            throws IOException
    {
        return rawCompress(input, input.length * 8); // double uses 8 bytes
    }