    private DDFFileParser(DDFFileValidator ddfValidator, DDFFileValidatorFactory ddfFileValidatorFactory) {
        factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        this.ddfValidator = ddfValidator;
        this.ddfValidatorFactory = ddfFileValidatorFactory;
    }