  public static void validateString(final String what, final String s, String specials) {
    if (s == null) {
      throw new BadRequestException("Invalid " + what + ": null");
    } else if ("".equals(s)) {
      throw new BadRequestException("Invalid " + what + ": empty string");
    }
    final int n = s.length();
    for (int i = 0; i < n; i++) {
      final char c = s.charAt(i);
      if (!(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')
          || ('0' <= c && c <= '9') || c == '-' || c == '_' || c == '.'
          || c == '/' || Character.isLetter(c) || specials.indexOf(c) != -1)) {
        throw new BadRequestException("Invalid " + what
            + " (\"" + s + "\"): illegal character: " + c);
      }
    }
  }