    public static Frame readFrom(DataInputStream is) throws IOException {
        int type;
        int channel;

        try {
            type = is.readUnsignedByte();
        } catch (SocketTimeoutException ste) {
            // System.err.println("Timed out waiting for a frame.");
            return null; // failed
        }

        if (type == 'A') {
            /*
             * Probably an AMQP.... header indicating a version
             * mismatch.
             */
            /*
             * Otherwise meaningless, so try to read the version,
             * and throw an exception, whether we read the version
             * okay or not.
             */
            protocolVersionMismatch(is);
        }

        channel = is.readUnsignedShort();
        int payloadSize = is.readInt();
        byte[] payload = new byte[payloadSize];
        is.readFully(payload);

        int frameEndMarker = is.readUnsignedByte();
        if (frameEndMarker != AMQP.FRAME_END) {
            throw new MalformedFrameException("Bad frame end marker: " + frameEndMarker);
        }

        return new Frame(type, channel, payload);
    }