    public static byte[] compress(char[] input)
            throws IOException
    {
        return rawCompress(input, input.length * 2); // char uses 2 bytes
    }