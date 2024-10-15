    private void registerUser() {
        Log.d("LOGIN_VM", "First launch");

        buttonRegisterOrUnlock.setOnClickListener(v -> {
            String passwordInput = passwordEntry.getText().toString();
            loginViewModel.createUser(passwordInput, encryptedSharedPreferences);
            VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Strong);
        });
    }