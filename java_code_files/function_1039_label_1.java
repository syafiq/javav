    private int decompressSequences(
            final Object inputBase, final long inputAddress, final long inputLimit,
            final Object outputBase, final long outputAddress, final long outputLimit,
            final Object literalsBase, final long literalsAddress, final long literalsLimit,
            long outputAbsoluteBaseAddress)
    {
        final long fastOutputLimit = outputLimit - SIZE_OF_LONG;
        final long fastMatchOutputLimit = fastOutputLimit - SIZE_OF_LONG;

        long input = inputAddress;
        long output = outputAddress;

        long literalsInput = literalsAddress;

        int size = (int) (inputLimit - inputAddress);
        verify(size >= MIN_SEQUENCES_SIZE, input, "Not enough input bytes");

        // decode header
        int sequenceCount = UNSAFE.getByte(inputBase, input++) & 0xFF;
        if (sequenceCount != 0) {
            if (sequenceCount == 255) {
                verify(input + SIZE_OF_SHORT <= inputLimit, input, "Not enough input bytes");
                sequenceCount = (UNSAFE.getShort(inputBase, input) & 0xFFFF) + LONG_NUMBER_OF_SEQUENCES;
                input += SIZE_OF_SHORT;
            }
            else if (sequenceCount > 127) {
                verify(input < inputLimit, input, "Not enough input bytes");
                sequenceCount = ((sequenceCount - 128) << 8) + (UNSAFE.getByte(inputBase, input++) & 0xFF);
            }

            verify(input + SIZE_OF_INT <= inputLimit, input, "Not enough input bytes");

            byte type = UNSAFE.getByte(inputBase, input++);

            int literalsLengthType = (type & 0xFF) >>> 6;
            int offsetCodesType = (type >>> 4) & 0b11;
            int matchLengthType = (type >>> 2) & 0b11;

            input = computeLiteralsTable(literalsLengthType, inputBase, input, inputLimit);
            input = computeOffsetsTable(offsetCodesType, inputBase, input, inputLimit);
            input = computeMatchLengthTable(matchLengthType, inputBase, input, inputLimit);

            // decompress sequences
            BitInputStream.Initializer initializer = new BitInputStream.Initializer(inputBase, input, inputLimit);
            initializer.initialize();
            int bitsConsumed = initializer.getBitsConsumed();
            long bits = initializer.getBits();
            long currentAddress = initializer.getCurrentAddress();

            FiniteStateEntropy.Table currentLiteralsLengthTable = this.currentLiteralsLengthTable;
            FiniteStateEntropy.Table currentOffsetCodesTable = this.currentOffsetCodesTable;
            FiniteStateEntropy.Table currentMatchLengthTable = this.currentMatchLengthTable;

            int literalsLengthState = (int) peekBits(bitsConsumed, bits, currentLiteralsLengthTable.log2Size);
            bitsConsumed += currentLiteralsLengthTable.log2Size;

            int offsetCodesState = (int) peekBits(bitsConsumed, bits, currentOffsetCodesTable.log2Size);
            bitsConsumed += currentOffsetCodesTable.log2Size;

            int matchLengthState = (int) peekBits(bitsConsumed, bits, currentMatchLengthTable.log2Size);
            bitsConsumed += currentMatchLengthTable.log2Size;

            int[] previousOffsets = this.previousOffsets;

            byte[] literalsLengthNumbersOfBits = currentLiteralsLengthTable.numberOfBits;
            int[] literalsLengthNewStates = currentLiteralsLengthTable.newState;
            byte[] literalsLengthSymbols = currentLiteralsLengthTable.symbol;

            byte[] matchLengthNumbersOfBits = currentMatchLengthTable.numberOfBits;
            int[] matchLengthNewStates = currentMatchLengthTable.newState;
            byte[] matchLengthSymbols = currentMatchLengthTable.symbol;

            byte[] offsetCodesNumbersOfBits = currentOffsetCodesTable.numberOfBits;
            int[] offsetCodesNewStates = currentOffsetCodesTable.newState;
            byte[] offsetCodesSymbols = currentOffsetCodesTable.symbol;

            while (sequenceCount > 0) {
                sequenceCount--;

                BitInputStream.Loader loader = new BitInputStream.Loader(inputBase, input, currentAddress, bits, bitsConsumed);
                loader.load();
                bitsConsumed = loader.getBitsConsumed();
                bits = loader.getBits();
                currentAddress = loader.getCurrentAddress();
                if (loader.isOverflow()) {
                    verify(sequenceCount == 0, input, "Not all sequences were consumed");
                    break;
                }

                // decode sequence
                int literalsLengthCode = literalsLengthSymbols[literalsLengthState];
                int matchLengthCode = matchLengthSymbols[matchLengthState];
                int offsetCode = offsetCodesSymbols[offsetCodesState];

                int literalsLengthBits = LITERALS_LENGTH_BITS[literalsLengthCode];
                int matchLengthBits = MATCH_LENGTH_BITS[matchLengthCode];
                int offsetBits = offsetCode;

                int offset = OFFSET_CODES_BASE[offsetCode];
                if (offsetCode > 0) {
                    offset += peekBits(bitsConsumed, bits, offsetBits);
                    bitsConsumed += offsetBits;
                }

                if (offsetCode <= 1) {
                    if (literalsLengthCode == 0) {
                        offset++;
                    }

                    if (offset != 0) {
                        int temp;
                        if (offset == 3) {
                            temp = previousOffsets[0] - 1;
                        }
                        else {
                            temp = previousOffsets[offset];
                        }

                        if (temp == 0) {
                            temp = 1;
                        }

                        if (offset != 1) {
                            previousOffsets[2] = previousOffsets[1];
                        }
                        previousOffsets[1] = previousOffsets[0];
                        previousOffsets[0] = temp;

                        offset = temp;
                    }
                    else {
                        offset = previousOffsets[0];
                    }
                }
                else {
                    previousOffsets[2] = previousOffsets[1];
                    previousOffsets[1] = previousOffsets[0];
                    previousOffsets[0] = offset;
                }

                int matchLength = MATCH_LENGTH_BASE[matchLengthCode];
                if (matchLengthCode > 31) {
                    matchLength += peekBits(bitsConsumed, bits, matchLengthBits);
                    bitsConsumed += matchLengthBits;
                }

                int literalsLength = LITERALS_LENGTH_BASE[literalsLengthCode];
                if (literalsLengthCode > 15) {
                    literalsLength += peekBits(bitsConsumed, bits, literalsLengthBits);
                    bitsConsumed += literalsLengthBits;
                }

                int totalBits = literalsLengthBits + matchLengthBits + offsetBits;
                if (totalBits > 64 - 7 - (LITERAL_LENGTH_TABLE_LOG + MATCH_LENGTH_TABLE_LOG + OFFSET_TABLE_LOG)) {
                    BitInputStream.Loader loader1 = new BitInputStream.Loader(inputBase, input, currentAddress, bits, bitsConsumed);
                    loader1.load();

                    bitsConsumed = loader1.getBitsConsumed();
                    bits = loader1.getBits();
                    currentAddress = loader1.getCurrentAddress();
                }

                int numberOfBits;

                numberOfBits = literalsLengthNumbersOfBits[literalsLengthState];
                literalsLengthState = (int) (literalsLengthNewStates[literalsLengthState] + peekBits(bitsConsumed, bits, numberOfBits)); // <= 9 bits
                bitsConsumed += numberOfBits;

                numberOfBits = matchLengthNumbersOfBits[matchLengthState];
                matchLengthState = (int) (matchLengthNewStates[matchLengthState] + peekBits(bitsConsumed, bits, numberOfBits)); // <= 9 bits
                bitsConsumed += numberOfBits;

                numberOfBits = offsetCodesNumbersOfBits[offsetCodesState];
                offsetCodesState = (int) (offsetCodesNewStates[offsetCodesState] + peekBits(bitsConsumed, bits, numberOfBits)); // <= 8 bits
                bitsConsumed += numberOfBits;

                final long literalOutputLimit = output + literalsLength;
                final long matchOutputLimit = literalOutputLimit + matchLength;

                verify(matchOutputLimit <= outputLimit, input, "Output buffer too small");
                long literalEnd = literalsInput + literalsLength;
                verify(literalEnd <= literalsLimit, input, "Input is corrupted");

                long matchAddress = literalOutputLimit - offset;
                verify(matchAddress >= outputAbsoluteBaseAddress, input, "Input is corrupted");

                if (literalOutputLimit > fastOutputLimit) {
                    executeLastSequence(outputBase, output, literalOutputLimit, matchOutputLimit, fastOutputLimit, literalsInput, matchAddress);
                }
                else {
                    // copy literals. literalOutputLimit <= fastOutputLimit, so we can copy
                    // long at a time with over-copy
                    output = copyLiterals(outputBase, literalsBase, output, literalsInput, literalOutputLimit);
                    copyMatch(outputBase, fastOutputLimit, output, offset, matchOutputLimit, matchAddress, matchLength, fastMatchOutputLimit);
                }
                output = matchOutputLimit;
                literalsInput = literalEnd;
            }
        }

        // last literal segment
        output = copyLastLiteral(outputBase, literalsBase, literalsLimit, output, literalsInput);

        return (int) (output - outputAddress);
    }