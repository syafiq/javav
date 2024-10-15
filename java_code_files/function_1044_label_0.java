    public void testInvalidLiteralLength()
    {
        byte[] data = {
                // Encoded uncompressed length 1024
                -128, 8,
                // op-code
                (byte) 252,
                // Trailer value Integer.MAX_VALUE
                (byte) 0b1111_1111, (byte) 0b1111_1111, (byte) 0b1111_1111, (byte) 0b0111_1111,
                // Some arbitrary data
                0, 0, 0, 0, 0, 0, 0, 0
        };

        assertThatThrownBy(() -> new SnappyDecompressor().decompress(data, 0, data.length, new byte[1024], 0, 1024))
                .isInstanceOf(MalformedInputException.class);
    }