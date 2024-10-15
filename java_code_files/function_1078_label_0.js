    public static String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);


        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = keyFactory.generateSecret(spec).getEncoded();

        String saltBase64 = Base64.encodeToString(salt, Base64.NO_WRAP);
        String hashBase64 = Base64.encodeToString(hash, Base64.NO_WRAP);

        return saltBase64 + ":" + hashBase64;
    }