    public void isInvalidChunkLengthForSnappyInputStreamOutOfMemory()
            throws Exception {
        byte[] data = {-126, 'S', 'N', 'A', 'P', 'P', 'Y', 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        SnappyInputStream in = new SnappyInputStream(new ByteArrayInputStream(data));
        byte[] out = new byte[50];
        try {
            in.read(out);
        } catch (Exception ignored) {
            // Exception here will be catched
            // But OutOfMemoryError will not be caught, and will still be thrown
        }
    }