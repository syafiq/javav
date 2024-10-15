    private static Stream<Arguments> invalidChars() {
        List<Arguments> invalid = new ArrayList<>();
        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            char c = (char) i;
            if (Character.isWhitespace(c)) {
                for (Position p: Position.values()) {
                    for (Part part: Part.values()) {
                        invalid.add(Arguments.of(p, part, "0x" + Integer.toHexString(c), c));
                    }
                }
            }
        }
        return invalid.stream();
    }