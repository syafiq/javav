    private void registerUser() {
        Log.d("LOGIN_VM", "First launch");

        buttonRegisterOrUnlock.setOnClickListener(v -> {
            String passwordInput = passwordEntry.getText().toString();
            try {
                loginViewModel.createUser(passwordInput, encryptedSharedPreferences);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
            VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Strong);
        });
    }