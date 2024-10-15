    private String loadingPage() {
        try {
            return IOUtils.toString(Objects.requireNonNull(getClass().getResource(systemEnvironment.get(LOADING_PAGE))), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "<h2>GoCD is starting up. Please wait ....</h2>";
        }
    }