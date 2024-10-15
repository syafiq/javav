    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SystemBarColorHelper.changeBarsColor(this, R.color.background_primary);

        initViews(binding);

        textViewRegisterOrUnlock.setText(getString(R.string.create_password_button_text));

        welcomeTextView.setText(getString(R.string.welcome_newpass_text));

        loginViewModel = new ViewModelProvider(this, new ViewMoldelsFactory(new ResourceRepository(getApplicationContext()))).get(LoginViewModel.class);

        loginViewModel.getLoginMessageLiveData().observe(this, message -> {
            // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        loginViewModel.getLoginSuccessLiveData().observe(this, success -> {
            String savedPasswordSharedPreferences = encryptedSharedPreferences.getString("password", "");

            if (success) {
                Intent intent = new Intent(LoginActivity.this, MainViewActivity.class);
                StringHelper.setSharedString(savedPasswordSharedPreferences);
                startActivity(intent);
                finish();
            } else {
                AnimationsUtility.errorAnimation(buttonRegisterOrUnlock, textViewRegisterOrUnlock);
            }
        });

        encryptedSharedPreferences = EncryptionHelper.getEncryptedSharedPreferences(getApplicationContext());

        //Determining whether to set dark or light mode based on shared preferences
        SharedPreferencesHelper.toggleDarkLightModeUI(this);

        String password = encryptedSharedPreferences.getString("password", "");
        Boolean isPasswordEmpty = password.isEmpty();

        if (!isPasswordEmpty) {
            textViewRegisterOrUnlock.setText(getString(R.string.unlock_newpass_button_text));
            welcomeTextView.setText(getString(R.string.welcome_back_newpass_text));

        } else {
            AlertDialog dialog = getAlertDialog();
            dialog.show();
        }

        buttonPasswordVisibility.setOnClickListener(v -> {

            if (isPasswordVisible) {
                buttonPasswordVisibility.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.icon_visibility_on));
                passwordEntry.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                buttonPasswordVisibility.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.icon_visibility_off));
                passwordEntry.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }

            isPasswordVisible = !isPasswordVisible;
        });


        buttonRegisterOrUnlockListener(buttonRegisterOrUnlock, isPasswordEmpty);
    }