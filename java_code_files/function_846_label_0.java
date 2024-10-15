    public void isInvalidChunkLengthForSnappyInputStream()
            throws Exception {
        byte[] data = {-126, 'S', 'N', 'A', 'P', 'P', 'Y', 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        SnappyInputStream in = new SnappyInputStream(new ByteArrayInputStream(data));
        byte[] out = new byte[50];
        try {
            in.read(out);
        } catch (SnappyError error) {
            Assert.assertEquals(error.errorCode, SnappyErrorCode.FAILED_TO_UNCOMPRESS);
        }
    }