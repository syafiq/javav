    public void loginUserWithPassword(String password, EncryptedSharedPreferences sharedPreferences) throws NoSuchAlgorithmException, InvalidKeySpecException {

        String hashedPassword = sharedPreferences.getString("password", "");

        if (PasswordUtils.verifyPassword(password, hashedPassword)) {
            loginSuccessLiveData.setValue(true);
            loginMessageLiveData.setValue(resourceRepository.getString(R.string.login_done));
        } else {
            loginSuccessLiveData.setValue(false);
            loginMessageLiveData.setValue(resourceRepository.getString(R.string.access_denied));
        }
    }