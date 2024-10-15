    private void loadingPageIsSetTo(String loadingPageResourcePath) {
        when(systemEnvironment.get(SystemEnvironment.LOADING_PAGE)).thenReturn(loadingPageResourcePath);
    }