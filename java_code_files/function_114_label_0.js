    public void validate_shouldNotMatchInvalidName() throws Exception {
        Assert.assertTrue(NameValidator.validate("John Doe<script />"));
    }