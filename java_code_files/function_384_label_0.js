    public void isValidArrayInputLength()
            throws Exception {
        byte[] a = Snappy.compress(new char[0]);
        byte[] b = Snappy.compress(new double[0]);
        byte[] c = Snappy.compress(new float[0]);
        byte[] d = Snappy.compress(new int[0]);
        byte[] e = Snappy.compress(new long[0]);
        byte[] f = Snappy.compress(new short[0]);
        byte[] g = Snappy.compress(new char[10]);
        byte[] h = Snappy.compress(new double[10]);
        byte[] i = Snappy.compress(new float[10]);
        byte[] j = Snappy.compress(new int[10]);
        byte[] k = Snappy.compress(new long[10]);
        byte[] l = Snappy.compress(new short[10]);
    }