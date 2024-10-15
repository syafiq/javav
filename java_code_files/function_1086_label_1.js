    public void loginUserWithPassword(String password, EncryptedSharedPreferences sharedPreferences) {

        String savedPasswordSharedPreferences = sharedPreferences.getString("password", "");

        if (savedPasswordSharedPreferences.equals(password)) {
            loginSuccessLiveData.setValue(true);
            loginMessageLiveData.setValue(resourceRepository.getString(R.string.login_done));
        } else {
            loginSuccessLiveData.setValue(false);
            loginMessageLiveData.setValue(resourceRepository.getString(R.string.access_denied));
        }
    }