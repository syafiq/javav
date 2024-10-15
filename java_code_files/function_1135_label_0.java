    private static void writeString(ByteBuf out, String str) {
        byte[] bytes = str.getBytes(CharsetUtil.US_ASCII);
        VarIntCodecUtils.writeVariableLengthInteger(out, bytes.length);
        out.writeBytes(bytes);
    }