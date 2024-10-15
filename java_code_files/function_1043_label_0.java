    public void testBadHuffmanData()
            throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        // Magic
        buffer.write(new byte[] {
                (byte) 0b0010_1000,
                (byte) 0b1011_0101,
                (byte) 0b0010_1111,
                (byte) 0b1111_1101,
        });
        // Frame header
        buffer.write(0);
        buffer.write(0);
        // Block header COMPRESSED_BLOCK
        buffer.write(new byte[] {
                (byte) 0b1111_0100,
                (byte) 0b0000_0000,
                (byte) 0b0000_0000,
        });
        // Literals header
        buffer.write(new byte[] {
                // literalsBlockType COMPRESSED_LITERALS_BLOCK
                // + literals type
                0b0000_1010,
                // ... header remainder
                0b0000_0000,
                // compressedSize
                0b0011_1100,
                0b0000_0000,
        });
        // Huffman inputSize
        buffer.write(128);
        // weight value
        buffer.write(0b0001_0000);
        // Bad start values
        buffer.write(new byte[] {(byte) 255, (byte) 255});
        buffer.write(new byte[] {(byte) 255, (byte) 255});
        buffer.write(new byte[] {(byte) 255, (byte) 255});

        buffer.write(new byte[10]);

        byte[] data = buffer.toByteArray();

        assertThatThrownBy(() -> new ZstdDecompressor().decompress(data, 0, data.length, new byte[10], 0, 10))
                .isInstanceOf(MalformedInputException.class)
                .hasMessageStartingWith("Input is corrupted");
    }