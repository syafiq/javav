        for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
            ALLOWED_TOKEN[128 + b] = !Character.isWhitespace(b);
        }