    static int[] readUncompressedLength(Object compressed, long compressedAddress, long compressedLimit)
    {
        int result;
        int bytesRead = 0;
        {
            int b = getUnsignedByteSafe(compressed, compressedAddress + bytesRead, compressedLimit);
            bytesRead++;
            result = b & 0x7f;
            if ((b & 0x80) != 0) {
                b = getUnsignedByteSafe(compressed, compressedAddress + bytesRead, compressedLimit);
                bytesRead++;
                result |= (b & 0x7f) << 7;
                if ((b & 0x80) != 0) {
                    b = getUnsignedByteSafe(compressed, compressedAddress + bytesRead, compressedLimit);
                    bytesRead++;
                    result |= (b & 0x7f) << 14;
                    if ((b & 0x80) != 0) {
                        b = getUnsignedByteSafe(compressed, compressedAddress + bytesRead, compressedLimit);
                        bytesRead++;
                        result |= (b & 0x7f) << 21;
                        if ((b & 0x80) != 0) {
                            b = getUnsignedByteSafe(compressed, compressedAddress + bytesRead, compressedLimit);
                            bytesRead++;
                            result |= (b & 0x7f) << 28;
                            if ((b & 0x80) != 0) {
                                throw new MalformedInputException(compressedAddress + bytesRead, "last byte of compressed length int has high bit set");
                            }
                        }
                    }
                }
            }
        }
        if (result < 0) {
            throw new MalformedInputException(compressedAddress, "negative compressed length");
        }
        return new int[] {result, bytesRead};
    }