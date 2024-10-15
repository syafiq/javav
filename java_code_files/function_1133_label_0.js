    private static BinaryHttpRequest readRequestHead(ByteBuf in, boolean knownLength, int maxFieldSectionSize) {
        if (!in.isReadable()) {
            return null;
        }

        // Check first if we can access all the control data for the request.
        int sumBytes = 0;
        final int methodLengthIdx = in.readerIndex() + sumBytes;
        final int methodLengthBytes = numBytesForVariableLengthIntegerFromByte(in.getByte(methodLengthIdx));
        sumBytes += methodLengthBytes;
        if (sumBytes >= in.readableBytes()) {
            return null;
        }

        final long methodLength = getVariableLengthInteger(in, methodLengthIdx, methodLengthBytes);
        sumBytes += methodLength;
        if (sumBytes >= in.readableBytes()) {
            return null;
        }
        final int methodIdx = methodLengthIdx + methodLengthBytes;

        final int schemeLengthIdx = in.readerIndex() + sumBytes;
        final int schemeLengthBytes = numBytesForVariableLengthIntegerFromByte(in.getByte(schemeLengthIdx));
        sumBytes += schemeLengthBytes;
        if (sumBytes >= in.readableBytes()) {
            return null;
        }

        final long schemeLength = getVariableLengthInteger(in, schemeLengthIdx, schemeLengthBytes);
        sumBytes += schemeLength;
        if (sumBytes >= in.readableBytes()) {
            return null;
        }
        final int schemeIdx = schemeLengthIdx + schemeLengthBytes;

        final int authorityLengthIdx = in.readerIndex() + sumBytes;
        final int authorityLengthBytes = numBytesForVariableLengthIntegerFromByte(in.getByte(authorityLengthIdx));
        sumBytes += authorityLengthBytes;
        if (sumBytes >= in.readableBytes()) {
            return null;
        }

        final long authorityLength = getVariableLengthInteger(in, authorityLengthIdx, authorityLengthBytes);
        sumBytes += authorityLength;
        if (sumBytes >= in.readableBytes()) {
            return null;
        }
        final int authorityIdx = authorityLengthIdx + authorityLengthBytes;

        final int pathLengthIdx = in.readerIndex() + sumBytes;
        final int pathLengthBytes = numBytesForVariableLengthIntegerFromByte(in.getByte(pathLengthIdx));
        sumBytes += pathLengthBytes;
        if (sumBytes >= in.readableBytes()) {
            return null;
        }

        final long pathLength = getVariableLengthInteger(in, pathLengthIdx, pathLengthBytes);
        sumBytes += pathLength;
        if (sumBytes >= in.readableBytes()) {
            return null;
        }
        final int pathIdx = pathLengthIdx + pathLengthBytes;

        // If we made it this far we had enough data for the whole control data.
        // Try to read the field section now.
        int fieldSectionIdx = in.readerIndex() + sumBytes;
        int fieldSectionLength = in.readableBytes() - sumBytes;
        ByteBuf fieldSectionSlice = in.slice(fieldSectionIdx, fieldSectionLength);

        int fieldSectionReadableBytes = fieldSectionSlice.readableBytes();
        BinaryHttpHeaders headers =
                readFieldSection(fieldSectionSlice, false, knownLength, maxFieldSectionSize);

        if (headers == null) {
            // We didn't have enough readable data to read the whole section, lets return and try again later.
            return null;
        }
        // Add the bytes of the field section as well.
        sumBytes += fieldSectionReadableBytes - fieldSectionSlice.readableBytes();

        // Let's validate method, scheme, authority and path.
        in.forEachByte(methodIdx, (int) methodLength, TOKEN_VALIDATOR);
        in.forEachByte(schemeIdx, (int) schemeLength, SCHEME_VALIDATOR);

        // We only do very limited validation for these to ensure there can nothing be injected.
        in.forEachByte(authorityIdx, (int) authorityLength, TOKEN_VALIDATOR);
        in.forEachByte(pathIdx, (int) pathLength, TOKEN_VALIDATOR);

        String method = in.toString(methodIdx, (int) methodLength, StandardCharsets.US_ASCII);
        String scheme = in.toString(schemeIdx, (int) schemeLength, StandardCharsets.US_ASCII);
        String authority = in.toString(authorityIdx, (int) authorityLength, StandardCharsets.US_ASCII);
        String path = in.toString(pathIdx, (int) pathLength, StandardCharsets.US_ASCII);

        BinaryHttpRequest request = new DefaultBinaryHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method),
                scheme, authority, path, headers);
        in.skipBytes(sumBytes);
        return request;
    }