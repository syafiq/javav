    public void isValidChunkLengthForSnappyInputStreamIn()
            throws Exception {
        byte[] data = {0};
        SnappyInputStream in = new SnappyInputStream(new ByteArrayInputStream(data));
        byte[] out = new byte[50];
        in.read(out);
    }