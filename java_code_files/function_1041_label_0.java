    public void testNegativeLength()
    {
        byte[] data = {(byte) 255, (byte) 255, (byte) 255, (byte) 255, 0b0000_1000};

        assertThatThrownBy(() -> SnappyDecompressor.getUncompressedLength(data, 0))
                .isInstanceOf(MalformedInputException.class)
                .hasMessageStartingWith("negative compressed length");
    }