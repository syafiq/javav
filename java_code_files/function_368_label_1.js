    protected boolean hasNextChunk()
            throws IOException
    {
        if (finishedReading) {
            return false;
        }

        uncompressedCursor = 0;
        uncompressedLimit = 0;

        int readBytes = readNext(header, 0, 4);
        if (readBytes < 4) {
            return false;
        }

        int chunkSize = SnappyOutputStream.readInt(header, 0);
        if (chunkSize == SnappyCodec.MAGIC_HEADER_HEAD) {
            // Concatenated data
            int remainingHeaderSize = SnappyCodec.headerSize() - 4;
            readBytes = readNext(header, 4, remainingHeaderSize);
            if(readBytes < remainingHeaderSize) {
                throw new SnappyIOException(SnappyErrorCode.FAILED_TO_UNCOMPRESS, String.format("Insufficient header size in a concatenated block"));
            }

            if (isValidHeader(header)) {
                return hasNextChunk();
            }
            else {
                return false;
            }
        }

        // extend the compressed data buffer size
        if (compressed == null || chunkSize > compressed.length) {
            compressed = new byte[chunkSize];
        }
        readBytes = 0;
        while (readBytes < chunkSize) {
            int ret = in.read(compressed, readBytes, chunkSize - readBytes);
            if (ret == -1) {
                break;
            }
            readBytes += ret;
        }
        if (readBytes < chunkSize) {
            throw new IOException("failed to read chunk");
        }
        int uncompressedLength = Snappy.uncompressedLength(compressed, 0, chunkSize);
        if (uncompressed == null || uncompressedLength > uncompressed.length) {
            uncompressed = new byte[uncompressedLength];
        }
        int actualUncompressedLength = Snappy.uncompress(compressed, 0, chunkSize, uncompressed, 0);
        if (uncompressedLength != actualUncompressedLength) {
            throw new SnappyIOException(SnappyErrorCode.INVALID_CHUNK_SIZE, String.format("expected %,d bytes, but decompressed chunk has %,d bytes", uncompressedLength, actualUncompressedLength));
        }
        uncompressedLimit = actualUncompressedLength;

        return true;
    }