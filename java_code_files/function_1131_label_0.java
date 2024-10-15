        if (b != 0) {
            throw new CorruptedFrameException(
                    "Invalid byte used for padding: '" + b + "' (0x" + Integer.toHexString(b) + ")");
        }