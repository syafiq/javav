    private static int uncompressAll(
            final Object inputBase,
            final long inputAddress,
            final long inputLimit,
            final Object outputBase,
            final long outputAddress,
            final long outputLimit)
    {
        final long fastOutputLimit = outputLimit - SIZE_OF_LONG; // maximum offset in output buffer to which it's safe to write long-at-a-time

        long output = outputAddress;
        long input = inputAddress;

        while (input < inputLimit) {
            int opCode = UNSAFE.getByte(inputBase, input++) & 0xFF;
            int entry = opLookupTable[opCode] & 0xFFFF;

            int trailerBytes = entry >>> 11;
            int trailer = 0;
            if (input + SIZE_OF_INT < inputLimit) {
                trailer = UNSAFE.getInt(inputBase, input) & wordmask[trailerBytes];
            }
            else {
                if (input + trailerBytes > inputLimit) {
                    throw new MalformedInputException(input - inputAddress);
                }
                switch (trailerBytes) {
                    case 4:
                        trailer = (UNSAFE.getByte(inputBase, input + 3) & 0xff) << 24;
                    case 3:
                        trailer |= (UNSAFE.getByte(inputBase, input + 2) & 0xff) << 16;
                    case 2:
                        trailer |= (UNSAFE.getByte(inputBase, input + 1) & 0xff) << 8;
                    case 1:
                        trailer |= (UNSAFE.getByte(inputBase, input) & 0xff);
                }
            }
            if (trailer < 0) {
                throw new MalformedInputException(input - inputAddress);
            }
            input += trailerBytes;

            int length = entry & 0xff;
            if (length == 0) {
                continue;
            }

            if ((opCode & 0x3) == LITERAL) {
                int literalLength = length + trailer;
                if (literalLength < 0) {
                    throw new MalformedInputException(input - inputAddress);
                }

                // copy literal
                long literalOutputLimit = output + literalLength;
                if (literalOutputLimit > fastOutputLimit || input + literalLength > inputLimit - SIZE_OF_LONG) {
                    if (literalOutputLimit > outputLimit || input + literalLength > inputLimit) {
                        throw new MalformedInputException(input - inputAddress);
                    }

                    // slow, precise copy
                    UNSAFE.copyMemory(inputBase, input, outputBase, output, literalLength);
                    input += literalLength;
                    output += literalLength;
                }
                else {
                    // fast copy. We may over-copy but there's enough room in input and output to not overrun them
                    do {
                        UNSAFE.putLong(outputBase, output, UNSAFE.getLong(inputBase, input));
                        input += SIZE_OF_LONG;
                        output += SIZE_OF_LONG;
                    }
                    while (output < literalOutputLimit);
                    input -= (output - literalOutputLimit); // adjust index if we over-copied
                    output = literalOutputLimit;
                }
            }
            else {
                // matchOffset/256 is encoded in bits 8..10.  By just fetching
                // those bits, we get matchOffset (since the bit-field starts at
                // bit 8).
                int matchOffset = entry & 0x700;
                matchOffset += trailer;
                if (matchOffset < 0) {
                    throw new MalformedInputException(input - inputAddress);
                }

                long matchAddress = output - matchOffset;
                if (matchAddress < outputAddress || output + length > outputLimit) {
                    throw new MalformedInputException(input - inputAddress);
                }
                long matchOutputLimit = output + length;
                if (matchOutputLimit > outputLimit) {
                    throw new MalformedInputException(input - inputAddress);
                }

                if (output > fastOutputLimit) {
                    // slow match copy
                    while (output < matchOutputLimit) {
                        UNSAFE.putByte(outputBase, output++, UNSAFE.getByte(outputBase, matchAddress++));
                    }
                }
                else {
                    // copy repeated sequence
                    if (matchOffset < SIZE_OF_LONG) {
                        // 8 bytes apart so that we can copy long-at-a-time below
                        int increment32 = DEC_32_TABLE[matchOffset];
                        int decrement64 = DEC_64_TABLE[matchOffset];

                        UNSAFE.putByte(outputBase, output, UNSAFE.getByte(outputBase, matchAddress));
                        UNSAFE.putByte(outputBase, output + 1, UNSAFE.getByte(outputBase, matchAddress + 1));
                        UNSAFE.putByte(outputBase, output + 2, UNSAFE.getByte(outputBase, matchAddress + 2));
                        UNSAFE.putByte(outputBase, output + 3, UNSAFE.getByte(outputBase, matchAddress + 3));
                        output += SIZE_OF_INT;
                        matchAddress += increment32;

                        UNSAFE.putInt(outputBase, output, UNSAFE.getInt(outputBase, matchAddress));
                        output += SIZE_OF_INT;
                        matchAddress -= decrement64;
                    }
                    else {
                        UNSAFE.putLong(outputBase, output, UNSAFE.getLong(outputBase, matchAddress));
                        matchAddress += SIZE_OF_LONG;
                        output += SIZE_OF_LONG;
                    }

                    if (matchOutputLimit > fastOutputLimit) {
                        while (output < fastOutputLimit) {
                            UNSAFE.putLong(outputBase, output, UNSAFE.getLong(outputBase, matchAddress));
                            matchAddress += SIZE_OF_LONG;
                            output += SIZE_OF_LONG;
                        }

                        while (output < matchOutputLimit) {
                            UNSAFE.putByte(outputBase, output++, UNSAFE.getByte(outputBase, matchAddress++));
                        }
                    }
                    else {
                        while (output < matchOutputLimit) {
                            UNSAFE.putLong(outputBase, output, UNSAFE.getLong(outputBase, matchAddress));
                            matchAddress += SIZE_OF_LONG;
                            output += SIZE_OF_LONG;
                        }
                    }
                }
                output = matchOutputLimit; // correction in case we over-copied
            }
        }

        return (int) (output - outputAddress);
    }