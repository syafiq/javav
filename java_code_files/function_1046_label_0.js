    public void testMatchLengthOverflow()
            throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write((byte) 0b0000_1111); // token
        buffer.write(new byte[2]); // offset

        // Causes overflow for `matchLength`
        byte[] literalLengthBytes = new byte[Integer.MAX_VALUE / 255 + 1]; // ~9MB
        Arrays.fill(literalLengthBytes, (byte) 255);
        buffer.write(literalLengthBytes);
        buffer.write(1);

        buffer.write(new byte[10]);

        byte[] data = buffer.toByteArray();

        assertThatThrownBy(() -> new Lz4Decompressor().decompress(data, 0, data.length, new byte[2048], 0, 2048))
                .isInstanceOf(MalformedInputException.class);
    }