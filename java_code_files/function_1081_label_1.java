    private void loginWithPassword(View view) {
        view.setOnTouchListener((v, event) -> {

            String passwordInput = passwordEntry.getText().toString();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Weak);
                    return true;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    loginViewModel.loginUserWithPassword(passwordInput, encryptedSharedPreferences);
                    VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Strong);
                    return true;
            }
            return false;
        });
    }