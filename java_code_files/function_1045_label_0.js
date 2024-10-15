    public void testLiteralLengthOverflow()
            throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        // Command
        buffer.write(0);
        // Causes overflow for `literalLength`
        buffer.write(new byte[Integer.MAX_VALUE / 255 + 1]); // ~9MB
        buffer.write(1);

        byte[] data = buffer.toByteArray();

        assertThatThrownBy(() -> new LzoDecompressor().decompress(data, 0, data.length, new byte[20000], 0, 20000))
                .isInstanceOf(MalformedInputException.class);
    }