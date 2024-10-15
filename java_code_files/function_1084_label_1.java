    public void createUser(String password, EncryptedSharedPreferences sharedPreferences) {

        if (password.length() >= 4) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("password", password);
            editor.apply();

            loginSuccessLiveData.setValue(true);
            loginMessageLiveData.setValue(resourceRepository.getString(R.string.user_created_successfully));
        } else {
            loginSuccessLiveData.setValue(false);
            loginMessageLiveData.setValue(resourceRepository.getString(R.string.password_must_be_at_least_4_characters_long));
        }
    }