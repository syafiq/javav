    public Optional<String> getRegistrationSuccessMessage()
    {
        List<WebElement> infos = getDriver().findElements(By.className("infomessage"));
        for (WebElement info : infos) {
            if (info.getText().contains("Registration successful.")) {
                return Optional.of(info.getText());
            }
        }

        return Optional.empty();
    }