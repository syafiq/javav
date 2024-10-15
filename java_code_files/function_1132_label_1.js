        if (b != 0) {
            throw new CorruptedFrameException("Invalid byte used for padding: " + b);
        }